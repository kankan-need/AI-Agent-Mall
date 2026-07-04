package com.learn.mall.agent.vo;

import java.util.List;
import java.util.Map;

public class AgentCompareVO {

    private List<AgentCompareSpecVO> specs;
    private List<AgentCompareItemVO> items;
    private Map<Long, List<String>> highlights;
    private String recommendation;

    public List<AgentCompareSpecVO> getSpecs() {
        return specs;
    }

    public void setSpecs(List<AgentCompareSpecVO> specs) {
        this.specs = specs;
    }

    public List<AgentCompareItemVO> getItems() {
        return items;
    }

    public void setItems(List<AgentCompareItemVO> items) {
        this.items = items;
    }

    public Map<Long, List<String>> getHighlights() {
        return highlights;
    }

    public void setHighlights(Map<Long, List<String>> highlights) {
        this.highlights = highlights;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }
}
