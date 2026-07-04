package com.learn.mall.agent.dto;

import java.util.List;

public class UserAgentPreferenceDTO {

    private String focusType;
    private Long budgetMin;
    private Long budgetMax;
    private List<String> tags;
    private String summary;

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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
