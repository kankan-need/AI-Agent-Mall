package com.learn.mall.agent.service;

import com.learn.mall.agent.dto.UserAgentPreferenceDTO;
import com.learn.mall.agent.vo.UserAgentPreferenceVO;

public interface UserAgentPreferenceService {

    UserAgentPreferenceVO getByUserId(Long userId);

    void save(Long userId, UserAgentPreferenceDTO dto, boolean fromAgent);

    String buildPreferencePrompt(Long userId);
}
