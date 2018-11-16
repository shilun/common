package com.common.config;

import com.common.util.StringUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class RedisCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        String property = conditionContext.getEnvironment().getProperty("spring.redis.url");
        String nodes = conditionContext.getEnvironment().getProperty("spring.redis.cluster.nodes");
        if (StringUtils.isNotBlank(property) || StringUtils.isNotBlank(nodes)) {
            return true;
        }

        return false;
    }
}
