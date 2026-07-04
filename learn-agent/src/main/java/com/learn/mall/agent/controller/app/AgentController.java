package com.learn.mall.agent.controller.app;

import com.learn.mall.agent.dto.AgentChatDTO;
import com.learn.mall.agent.dto.UserAgentPreferenceDTO;
import com.learn.mall.agent.service.AgentChatService;
import com.learn.mall.agent.service.UserAgentPreferenceService;
import com.learn.mall.agent.vo.AgentChatResponseVO;
import com.learn.mall.agent.vo.UserAgentPreferenceVO;
import com.learn.mall.common.response.ServerResponseEntity;
import com.learn.mall.common.security.AuthUserContext;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/a/agent")
public class AgentController {

    @Autowired
    private AgentChatService agentChatService;
    @Autowired
    private UserAgentPreferenceService preferenceService;

    @PostMapping("/chat")
    public ServerResponseEntity<AgentChatResponseVO> chat(@Valid @RequestBody AgentChatDTO dto) {
        Long userId = AuthUserContext.get().getUserId();
        return ServerResponseEntity.success(agentChatService.chat(userId, dto.getMessage()));
    }

    @GetMapping("/preference")
    public ServerResponseEntity<UserAgentPreferenceVO> getPreference() {
        Long userId = AuthUserContext.get().getUserId();
        return ServerResponseEntity.success(preferenceService.getByUserId(userId));
    }

    @PutMapping("/preference")
    public ServerResponseEntity<Void> updatePreference(@RequestBody UserAgentPreferenceDTO dto) {
        Long userId = AuthUserContext.get().getUserId();
        preferenceService.save(userId, dto, false);
        return ServerResponseEntity.success();
    }
}
