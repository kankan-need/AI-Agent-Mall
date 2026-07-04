package com.learn.mall.agent.vo;

public class AgentCompareSpecVO {

    private String key;
    private String label;

    public AgentCompareSpecVO() {
    }

    public AgentCompareSpecVO(String key, String label) {
        this.key = key;
        this.label = label;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
