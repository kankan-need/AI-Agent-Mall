package com.learn.mall.agent.model;

import java.time.LocalDateTime;

public class UserAgentPreference {

    private Long userId;
    private String focusType;
    private Long budgetMin;
    private Long budgetMax;
    private String tags;
    private String summary;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFocusType() {
        return focusType;
    }

    public void setFocusType(String focusType) {
        this.focusType = focusType;
    }

    public Long getBudgetMin() {
        return budgetMin;
    }

    public void setBudgetMin(Long budgetMin) {
        this.budgetMin = budgetMin;
    }

    public Long getBudgetMax() {
        return budgetMax;
    }

    public void setBudgetMax(Long budgetMax) {
        this.budgetMax = budgetMax;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
