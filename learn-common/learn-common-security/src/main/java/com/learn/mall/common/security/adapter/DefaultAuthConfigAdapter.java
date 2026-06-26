package com.learn.mall.common.security.adapter;

import com.learn.mall.common.feign.FeignInsideAuthConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DefaultAuthConfigAdapter implements AuthConfigAdapter {

    private static final String FEIGN_INSIDER_URI = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/**";
    private static final String EXTERNAL_URI = "/ua/**";
    private static final String ACTUATOR_URI = "/actuator/**";

    @Override
    public List<String> pathPatterns() {
        return Collections.singletonList("/*");
    }

    @Override
    public List<String> excludePathPatterns(String... paths) {
        List<String> patterns = new ArrayList<>();
        patterns.add(FEIGN_INSIDER_URI);
        patterns.add(EXTERNAL_URI);
        patterns.add(ACTUATOR_URI);
        patterns.addAll(Arrays.asList(paths));
        return patterns;
    }
}
