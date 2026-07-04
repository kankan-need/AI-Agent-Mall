package com.learn.mall.agent.service;

import com.learn.mall.agent.bo.AgentChatMessageBO;

import java.util.List;

public interface AgentChatHistoryService {

    List<AgentChatMessageBO> listRecent(Long userId);

    void append(Long userId, String userMessage, String assistantReply);
}
