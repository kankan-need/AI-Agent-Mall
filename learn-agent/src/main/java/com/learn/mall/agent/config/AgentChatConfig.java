package com.learn.mall.agent.config;

import com.learn.mall.agent.tool.AgentTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AgentChatConfig {

    public static final String SYSTEM_PROMPT = """
            你是 Learn Mall 购物助手，使用中文回复。
            工作流程：
            1. 理解用户需求（功能描述、品类、预算、品质/性价比倾向）。
            2. 先调用 getUserPreference 了解用户偏好；若用户表达新偏好，调用 updateUserPreference 保存。
            3. 使用 searchProducts 搜索候选商品（关键词从需求提取，可多次搜索）。
            4. 必要时用 getProductBriefs 获取商品详情。
            5. 推荐 2~4 款最匹配商品，结合用户偏好说明理由，并为每款给出 2~3 条优点和 1~2 条缺点。
            6. 最后必须调用 submitRecommendations 提交推荐结果。
            注意：
            - 用户可能不知道商品名称，需根据功能/场景从名称和卖点中识别合适商品。
            - 关注类型 focusType：VALUE=性价比，QUALITY=品质，BALANCED=均衡。
            - 价格单位：数据库价格为分，与用户交流时用元。
            - 若无合适商品，仍要友好回复并引导用户补充需求。
            """;

    @Bean
    public ChatClient agentChatClient(ChatModel chatModel, AgentTools agentTools) {
        return ChatClient.builder(chatModel)
                .defaultTools(agentTools)
                .build();
    }
}
