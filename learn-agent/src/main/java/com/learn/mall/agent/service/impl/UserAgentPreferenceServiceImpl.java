package com.learn.mall.agent.service.impl;

import cn.hutool.core.util.StrUtil;
import com.learn.mall.agent.constant.AgentFocusType;
import com.learn.mall.agent.context.AgentChatContext;
import com.learn.mall.agent.dto.UserAgentPreferenceDTO;
import com.learn.mall.agent.mapper.UserAgentPreferenceMapper;
import com.learn.mall.agent.model.UserAgentPreference;
import com.learn.mall.agent.service.UserAgentPreferenceService;
import com.learn.mall.agent.vo.UserAgentPreferenceVO;
import com.learn.mall.common.util.BeanUtil;
import com.learn.mall.common.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.type.TypeReference;

import java.util.Collections;
import java.util.List;

@Service
public class UserAgentPreferenceServiceImpl implements UserAgentPreferenceService {

    @Autowired
    private UserAgentPreferenceMapper preferenceMapper;

    @Override
    public UserAgentPreferenceVO getByUserId(Long userId) {
        UserAgentPreference preference = preferenceMapper.getByUserId(userId);
        if (preference == null) {
            UserAgentPreferenceVO vo = new UserAgentPreferenceVO();
            vo.setFocusType(AgentFocusType.BALANCED);
            vo.setTags(Collections.emptyList());
            vo.setSummary("暂无偏好记录，多和购物助手聊聊吧");
            return vo;
        }
        return toVo(preference);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Long userId, UserAgentPreferenceDTO dto, boolean fromAgent) {
        UserAgentPreference existing = preferenceMapper.getByUserId(userId);
        UserAgentPreference preference = new UserAgentPreference();
        preference.setUserId(userId);
        preference.setFocusType(normalizeFocusType(dto.getFocusType()));
        preference.setBudgetMin(dto.getBudgetMin());
        preference.setBudgetMax(dto.getBudgetMax());
        preference.setTags(toTagsJson(dto.getTags()));
        preference.setSummary(StrUtil.blankToDefault(dto.getSummary(), ""));

        if (existing == null) {
            preferenceMapper.insert(preference);
        } else {
            preferenceMapper.update(preference);
        }
        if (fromAgent) {
            AgentChatContext.markPreferenceUpdated();
        }
    }

    @Override
    public String buildPreferencePrompt(Long userId) {
        UserAgentPreferenceVO vo = getByUserId(userId);
        StringBuilder sb = new StringBuilder();
        sb.append("用户当前购物偏好：\n");
        sb.append("- 关注类型：").append(focusTypeLabel(vo.getFocusType())).append('\n');
        if (vo.getBudgetMin() != null || vo.getBudgetMax() != null) {
            sb.append("- 预算区间：")
                    .append(formatPrice(vo.getBudgetMin()))
                    .append(" ~ ")
                    .append(formatPrice(vo.getBudgetMax()))
                    .append('\n');
        }
        if (vo.getTags() != null && !vo.getTags().isEmpty()) {
            sb.append("- 标签：").append(String.join("、", vo.getTags())).append('\n');
        }
        if (StrUtil.isNotBlank(vo.getSummary())) {
            sb.append("- 摘要：").append(vo.getSummary()).append('\n');
        }
        return sb.toString();
    }

    private UserAgentPreferenceVO toVo(UserAgentPreference preference) {
        UserAgentPreferenceVO vo = BeanUtil.map(preference, UserAgentPreferenceVO.class);
        vo.setTags(parseTags(preference.getTags()));
        return vo;
    }

    private List<String> parseTags(String tagsJson) {
        if (StrUtil.isBlank(tagsJson)) {
            return Collections.emptyList();
        }
        List<String> tags = Json.parseObject(tagsJson, new TypeReference<List<String>>() {
        });
        return tags == null ? Collections.emptyList() : tags;
    }

    private String toTagsJson(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return "[]";
        }
        return Json.toJsonString(tags);
    }

    private String normalizeFocusType(String focusType) {
        if (AgentFocusType.VALUE.equals(focusType) || AgentFocusType.QUALITY.equals(focusType)) {
            return focusType;
        }
        return AgentFocusType.BALANCED;
    }

    private String focusTypeLabel(String focusType) {
        if (AgentFocusType.VALUE.equals(focusType)) {
            return "性价比优先";
        }
        if (AgentFocusType.QUALITY.equals(focusType)) {
            return "品质优先";
        }
        return "均衡";
    }

    private String formatPrice(Long fee) {
        if (fee == null) {
            return "不限";
        }
        return String.format("%.2f元", fee / 100.0);
    }
}
