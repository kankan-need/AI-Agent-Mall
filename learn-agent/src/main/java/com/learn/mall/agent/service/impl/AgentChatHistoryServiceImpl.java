package com.learn.mall.agent.service.impl;

import cn.hutool.core.util.StrUtil;
import com.learn.mall.agent.bo.AgentChatMessageBO;
import com.learn.mall.agent.service.AgentChatHistoryService;
import com.learn.mall.common.util.Json;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class AgentChatHistoryServiceImpl implements AgentChatHistoryService {

    private static final String KEY_PREFIX = "agent:chat:";

    private final StringRedisTemplate stringRedisTemplate;
    private final int historySize;

    public AgentChatHistoryServiceImpl(StringRedisTemplate stringRedisTemplate,
                                       @Value("${learn.mall.agent.chat-history-size:20}") int historySize) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.historySize = historySize;
    }

    @Override
    public List<AgentChatMessageBO> listRecent(Long userId) {
        String json = stringRedisTemplate.opsForValue().get(KEY_PREFIX + userId);
        if (StrUtil.isBlank(json)) {
            return List.of();
        }
        List<AgentChatMessageBO> messages = Json.parseObject(json, new TypeReference<List<AgentChatMessageBO>>() {
        });
        return messages == null ? List.of() : messages;
    }

    @Override
    public void append(Long userId, String userMessage, String assistantReply) {
        List<AgentChatMessageBO> messages = new ArrayList<>(listRecent(userId));
        messages.add(new AgentChatMessageBO("user", userMessage));
        messages.add(new AgentChatMessageBO("assistant", assistantReply));
        while (messages.size() > historySize) {
            messages.remove(0);
        }
        stringRedisTemplate.opsForValue().set(KEY_PREFIX + userId, Json.toJsonString(messages), Duration.ofDays(30));
    }
}
