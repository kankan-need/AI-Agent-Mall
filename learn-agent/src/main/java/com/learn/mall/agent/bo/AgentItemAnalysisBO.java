package com.learn.mall.agent.bo;

import java.util.ArrayList;
import java.util.List;

public class AgentItemAnalysisBO {

    private Long spuId;
    private List<String> pros = new ArrayList<>();
    private List<String> cons = new ArrayList<>();
    private Boolean recommended;

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public List<String> getPros() {
        return pros;
    }

    public void setPros(List<String> pros) {
        this.pros = pros;
    }

    public List<String> getCons() {
        return cons;
    }

    public void setCons(List<String> cons) {
        this.cons = cons;
    }

    public Boolean getRecommended() {
        return recommended;
    }

    public void setRecommended(Boolean recommended) {
        this.recommended = recommended;
    }
}
