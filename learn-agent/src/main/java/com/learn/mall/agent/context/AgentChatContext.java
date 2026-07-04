package com.learn.mall.agent.context;

import com.learn.mall.agent.bo.AgentRecommendationBO;

public final class AgentChatContext {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> PREFERENCE_UPDATED = ThreadLocal.withInitial(() -> false);
    private static final ThreadLocal<AgentRecommendationBO> RECOMMENDATION = new ThreadLocal<>();

    private AgentChatContext() {
    }

    public static void init(Long userId) {
        USER_ID.set(userId);
        PREFERENCE_UPDATED.set(false);
        RECOMMENDATION.remove();
    }

    public static Long getUserId() {
        return USER_ID.get();
    }

    public static void markPreferenceUpdated() {
        PREFERENCE_UPDATED.set(true);
    }

    public static boolean isPreferenceUpdated() {
        return Boolean.TRUE.equals(PREFERENCE_UPDATED.get());
    }

    public static void setRecommendation(AgentRecommendationBO recommendation) {
        RECOMMENDATION.set(recommendation);
    }

    public static AgentRecommendationBO getRecommendation() {
        return RECOMMENDATION.get();
    }

    public static void clear() {
        USER_ID.remove();
        PREFERENCE_UPDATED.remove();
        RECOMMENDATION.remove();
    }
}
