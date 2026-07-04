package com.learn.mall.agent.mapper;

import com.learn.mall.agent.model.UserAgentPreference;
import org.apache.ibatis.annotations.Param;

public interface UserAgentPreferenceMapper {

    UserAgentPreference getByUserId(@Param("userId") Long userId);

    int insert(UserAgentPreference preference);

    int update(UserAgentPreference preference);
}
