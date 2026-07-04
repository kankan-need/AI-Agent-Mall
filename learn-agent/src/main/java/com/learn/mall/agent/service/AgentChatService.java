package com.learn.mall.agent.service;

import com.learn.mall.agent.vo.AgentChatResponseVO;

public interface AgentChatService {

    AgentChatResponseVO chat(Long userId, String message);
}
