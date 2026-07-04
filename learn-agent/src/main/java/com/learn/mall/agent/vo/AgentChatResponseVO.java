package com.learn.mall.agent.vo;

import java.util.List;

public class AgentChatResponseVO {

    private String reply;
    private List<AgentProductVO> products;
    private AgentCompareVO compare;
    private Boolean preferenceUpdated;

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public List<AgentProductVO> getProducts() {
        return products;
    }

    public void setProducts(List<AgentProductVO> products) {
        this.products = products;
    }

    public AgentCompareVO getCompare() {
        return compare;
    }

    public void setCompare(AgentCompareVO compare) {
        this.compare = compare;
    }

    public Boolean getPreferenceUpdated() {
        return preferenceUpdated;
    }

    public void setPreferenceUpdated(Boolean preferenceUpdated) {
        this.preferenceUpdated = preferenceUpdated;
    }
}
