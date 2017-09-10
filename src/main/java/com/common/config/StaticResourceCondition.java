package com.common.config;

import com.common.util.StringUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class StaticResourceCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        String property = conditionContext.getEnvironment().getProperty("proxy.nodejs.servlet_url");
        String staticPath = conditionContext.getEnvironment().getProperty("app.static.path");
        if (StringUtils.isBlank(property) && StringUtils.isBlank(staticPath)) {
            return true;
        }
        return false;
    }
}
