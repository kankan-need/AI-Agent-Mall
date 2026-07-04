package com.learn.mall.agent.service.impl;

import cn.hutool.core.util.StrUtil;
import com.learn.mall.agent.bo.AgentChatMessageBO;
import com.learn.mall.agent.bo.AgentRecommendationBO;
import com.learn.mall.agent.config.AgentChatConfig;
import com.learn.mall.agent.context.AgentChatContext;
import com.learn.mall.agent.service.AgentChatHistoryService;
import com.learn.mall.agent.service.AgentChatService;
import com.learn.mall.agent.service.UserAgentPreferenceService;
import com.learn.mall.agent.support.AgentCompareBuilder;
import com.learn.mall.agent.vo.AgentChatResponseVO;
import com.learn.mall.agent.vo.AgentCompareVO;
import com.learn.mall.api.product.feign.ProductFeignClient;
import com.learn.mall.api.product.vo.SpuAgentBriefVO;
import com.learn.mall.common.response.ServerResponseEntity;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AgentChatServiceImpl implements AgentChatService {

    @Autowired
    private ChatClient agentChatClient;
    @Autowired
    private AgentChatHistoryService chatHistoryService;
    @Autowired
    private UserAgentPreferenceService preferenceService;
    @Autowired
    private ProductFeignClient productFeignClient;

    @Override
    public AgentChatResponseVO chat(Long userId, String message) {
        AgentChatContext.init(userId);
        try {
            List<AgentChatMessageBO> history = chatHistoryService.listRecent(userId);
            String preferencePrompt = preferenceService.buildPreferencePrompt(userId);

            List<Message> messageList = new ArrayList<>();
            messageList.add(new SystemMessage(AgentChatConfig.SYSTEM_PROMPT + "\n\n" + preferencePrompt));
            for (AgentChatMessageBO item : history) {
                if ("user".equals(item.getRole())) {
                    messageList.add(new UserMessage(item.getContent()));
                } else if ("assistant".equals(item.getRole())) {
                    messageList.add(new AssistantMessage(item.getContent()));
                }
            }
            messageList.add(new UserMessage(message));

            String reply = agentChatClient.prompt()
                    .messages(messageList)
                    .call()
                    .content();
            AgentRecommendationBO recommendation = AgentChatContext.getRecommendation();

            AgentChatResponseVO responseVO = new AgentChatResponseVO();
            responseVO.setReply(StrUtil.blankToDefault(reply, "已为你分析完成，请查看推荐商品。"));
            responseVO.setPreferenceUpdated(AgentChatContext.isPreferenceUpdated());

            if (recommendation != null && recommendation.getProductIds() != null && !recommendation.getProductIds().isEmpty()) {
                List<SpuAgentBriefVO> briefs = loadBriefs(recommendation.getProductIds());
                responseVO.setProducts(AgentCompareBuilder.toProductVoList(briefs));
                AgentCompareVO compare = AgentCompareBuilder.buildCompare(recommendation, briefs);
                responseVO.setCompare(compare);
            } else {
                responseVO.setProducts(List.of());
            }

            chatHistoryService.append(userId, message, responseVO.getReply());
            return responseVO;
        } finally {
            AgentChatContext.clear();
        }
    }

    private List<SpuAgentBriefVO> loadBriefs(List<Long> productIds) {
        ServerResponseEntity<List<SpuAgentBriefVO>> response = productFeignClient.listBriefsByIds(productIds);
        if (response == null || !response.isSuccess() || response.getData() == null) {
            return List.of();
        }
        Map<Long, SpuAgentBriefVO> map = response.getData().stream()
                .collect(Collectors.toMap(SpuAgentBriefVO::getSpuId, Function.identity(), (a, b) -> a, LinkedHashMap::new));
        List<SpuAgentBriefVO> ordered = new ArrayList<>();
        for (Long productId : productIds) {
            SpuAgentBriefVO brief = map.get(productId);
            if (brief != null) {
                ordered.add(brief);
            }
        }
        return ordered;
    }
}
