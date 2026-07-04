package com.learn.mall.agent.tool;

import cn.hutool.core.util.StrUtil;
import com.learn.mall.agent.bo.AgentItemAnalysisBO;
import com.learn.mall.agent.bo.AgentRecommendationBO;
import com.learn.mall.agent.context.AgentChatContext;
import com.learn.mall.agent.dto.UserAgentPreferenceDTO;
import com.learn.mall.agent.service.UserAgentPreferenceService;
import com.learn.mall.agent.vo.UserAgentPreferenceVO;
import com.learn.mall.api.product.feign.ProductFeignClient;
import com.learn.mall.api.product.vo.SpuAgentBriefVO;
import com.learn.mall.common.response.ServerResponseEntity;
import com.learn.mall.common.util.Json;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;

import java.util.List;

@Component
public class AgentTools {

    @Autowired
    private ProductFeignClient productFeignClient;
    @Autowired
    private UserAgentPreferenceService preferenceService;

    @Tool(description = "根据关键词搜索上架商品，匹配商品名称和卖点描述")
    public List<SpuAgentBriefVO> searchProducts(
            @ToolParam(description = "搜索关键词，可从用户需求中提取") String keyword,
            @ToolParam(description = "返回数量，建议2-8") Integer limit) {
        int safeLimit = limit == null ? 8 : Math.min(Math.max(limit, 1), 12);
        ServerResponseEntity<List<SpuAgentBriefVO>> response = productFeignClient.searchForAgent(keyword, safeLimit);
        if (response == null || !response.isSuccess() || response.getData() == null) {
            return List.of();
        }
        return response.getData();
    }

    @Tool(description = "根据商品ID批量获取商品摘要信息")
    public List<SpuAgentBriefVO> getProductBriefs(
            @ToolParam(description = "商品ID列表") List<Long> spuIds) {
        if (spuIds == null || spuIds.isEmpty()) {
            return List.of();
        }
        ServerResponseEntity<List<SpuAgentBriefVO>> response = productFeignClient.listBriefsByIds(spuIds);
        if (response == null || !response.isSuccess() || response.getData() == null) {
            return List.of();
        }
        return response.getData();
    }

    @Tool(description = "获取当前登录用户的购物偏好")
    public UserAgentPreferenceVO getUserPreference() {
        Long userId = AgentChatContext.getUserId();
        return preferenceService.getByUserId(userId);
    }

    @Tool(description = "更新当前登录用户的购物偏好，当用户表达预算、品质/性价比倾向时调用")
    public String updateUserPreference(
            @ToolParam(description = "关注类型：VALUE(性价比)/QUALITY(品质)/BALANCED(均衡)") String focusType,
            @ToolParam(description = "预算下限(分)，可空") Long budgetMin,
            @ToolParam(description = "预算上限(分)，可空") Long budgetMax,
            @ToolParam(description = "偏好标签，如轻便、送礼") List<String> tags,
            @ToolParam(description = "一句话偏好摘要") String summary) {
        Long userId = AgentChatContext.getUserId();
        UserAgentPreferenceDTO dto = new UserAgentPreferenceDTO();
        dto.setFocusType(focusType);
        dto.setBudgetMin(budgetMin);
        dto.setBudgetMax(budgetMax);
        dto.setTags(tags);
        dto.setSummary(summary);
        preferenceService.save(userId, dto, true);
        return "偏好已更新";
    }

    @Tool(description = "提交最终推荐结果，完成商品分析后必须调用一次")
    public String submitRecommendations(
            @ToolParam(description = "推荐商品ID列表，按优先级排序") List<Long> productIds,
            @ToolParam(description = "总结推荐语") String recommendation,
            @ToolParam(description = "JSON数组，格式[{spuId,pros:[],cons:[],recommended:true/false}]") String itemAnalysisJson) {
        AgentRecommendationBO recommendationBO = new AgentRecommendationBO();
        recommendationBO.setProductIds(productIds == null ? List.of() : productIds);
        recommendationBO.setRecommendation(recommendation);
        recommendationBO.setItems(parseItemAnalysis(itemAnalysisJson));
        AgentChatContext.setRecommendation(recommendationBO);
        return "推荐结果已提交";
    }

    private List<AgentItemAnalysisBO> parseItemAnalysis(String itemAnalysisJson) {
        if (StrUtil.isBlank(itemAnalysisJson)) {
            return List.of();
        }
        try {
            List<AgentItemAnalysisBO> items = Json.parseObject(itemAnalysisJson, new TypeReference<List<AgentItemAnalysisBO>>() {
            });
            return items == null ? List.of() : items;
        } catch (Exception ex) {
            return List.of();
        }
    }
}
