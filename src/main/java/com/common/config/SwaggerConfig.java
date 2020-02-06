package com.common.config;

import com.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Conditional(SwaggerCondition.class)
public class SwaggerConfig {
    @Value("${app.swagger.controller}")
    private String swagger;
    @Bean
    public Docket createRestApi() {
        Docket build = new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().apis(RequestHandlerSelectors.basePackage(swagger)).paths(PathSelectors.any()).build();
        build.ignoredParameterTypes(Pageable.class, Sort.class);
        if (StringUtils.isBlank(swagger)) {
            build.enable(false);
        }
        return build;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("api文档").version("1.0").build();
    }


}

