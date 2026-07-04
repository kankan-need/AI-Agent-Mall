package com.learn.mall.agent.support;

import com.learn.mall.agent.bo.AgentItemAnalysisBO;
import com.learn.mall.agent.bo.AgentRecommendationBO;
import com.learn.mall.agent.vo.AgentCompareItemVO;
import com.learn.mall.agent.vo.AgentCompareSpecVO;
import com.learn.mall.agent.vo.AgentCompareVO;
import com.learn.mall.agent.vo.AgentProductVO;
import com.learn.mall.api.product.vo.SpuAgentBriefVO;
import com.learn.mall.common.util.BeanUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class AgentCompareBuilder {

    private AgentCompareBuilder() {
    }

    public static List<AgentProductVO> toProductVoList(List<SpuAgentBriefVO> briefs) {
        List<AgentProductVO> list = new ArrayList<>(briefs.size());
        for (SpuAgentBriefVO brief : briefs) {
            list.add(BeanUtil.map(brief, AgentProductVO.class));
        }
        return list;
    }

    public static AgentCompareVO buildCompare(AgentRecommendationBO recommendation, List<SpuAgentBriefVO> briefs) {
        if (recommendation == null || briefs == null || briefs.isEmpty()) {
            return null;
        }
        Map<Long, SpuAgentBriefVO> briefMap = briefs.stream()
                .collect(Collectors.toMap(SpuAgentBriefVO::getSpuId, Function.identity(), (a, b) -> a, LinkedHashMap::new));
        Map<Long, AgentItemAnalysisBO> analysisMap = recommendation.getItems().stream()
                .filter(item -> item.getSpuId() != null)
                .collect(Collectors.toMap(AgentItemAnalysisBO::getSpuId, Function.identity(), (a, b) -> a));

        List<AgentCompareSpecVO> specs = List.of(
                new AgentCompareSpecVO("price", "价格"),
                new AgentCompareSpecVO("point", "卖点"),
                new AgentCompareSpecVO("sales", "销量"),
                new AgentCompareSpecVO("pros", "优点"),
                new AgentCompareSpecVO("cons", "缺点")
        );

        List<AgentCompareItemVO> items = new ArrayList<>();
        Map<Long, List<String>> highlights = new HashMap<>();
        List<Long> orderedIds = recommendation.getProductIds().isEmpty()
                ? briefs.stream().map(SpuAgentBriefVO::getSpuId).toList()
                : recommendation.getProductIds();

        for (Long spuId : orderedIds) {
            SpuAgentBriefVO brief = briefMap.get(spuId);
            if (brief == null) {
                continue;
            }
            AgentItemAnalysisBO analysis = analysisMap.get(spuId);
            AgentCompareItemVO item = new AgentCompareItemVO();
            item.setSpuId(spuId);
            item.setName(brief.getName());
            item.setRecommended(analysis != null && Boolean.TRUE.equals(analysis.getRecommended()));

            Map<String, String> values = new LinkedHashMap<>();
            values.put("price", formatPrice(brief.getPriceFee()));
            values.put("point", defaultText(brief.getSellingPoint()));
            values.put("sales", String.valueOf(brief.getSaleNum() == null ? 0 : brief.getSaleNum()));
            values.put("pros", joinList(analysis == null ? null : analysis.getPros()));
            values.put("cons", joinList(analysis == null ? null : analysis.getCons()));
            item.setValues(values);
            items.add(item);

            if (item.getRecommended()) {
                highlights.put(spuId, List.of("price", "sales", "pros"));
            }
        }

        AgentCompareVO compareVO = new AgentCompareVO();
        compareVO.setSpecs(specs);
        compareVO.setItems(items);
        compareVO.setHighlights(highlights);
        compareVO.setRecommendation(recommendation.getRecommendation());
        return compareVO;
    }

    private static String formatPrice(Long priceFee) {
        if (priceFee == null) {
            return "0.00";
        }
        return String.format("%.2f", priceFee / 100.0);
    }

    private static String defaultText(String text) {
        return text == null ? "-" : text;
    }

    private static String joinList(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "-";
        }
        return String.join("；", list);
    }
}
