package com.learn.mall.agent.dto;

import jakarta.validation.constraints.NotBlank;

public class AgentChatDTO {

    @NotBlank(message = "message不能为空")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
