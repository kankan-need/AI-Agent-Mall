package com.learn.mall.agent.bo;

import java.util.ArrayList;
import java.util.List;

public class AgentRecommendationBO {

    private List<Long> productIds = new ArrayList<>();
    private List<AgentItemAnalysisBO> items = new ArrayList<>();
    private String recommendation;

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }

    public List<AgentItemAnalysisBO> getItems() {
        return items;
    }

    public void setItems(List<AgentItemAnalysisBO> items) {
        this.items = items;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }
}
