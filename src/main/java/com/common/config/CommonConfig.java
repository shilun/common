package com.common.config;

import com.common.fastxml.MoneySerialize;
import com.common.util.Money;
import com.common.util.RPCResult;
import com.common.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Slf4j
public class CommonConfig implements WebMvcConfigurer {
    @Bean
    public ObjectMapper ObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        SimpleModule module = new SimpleModule();
        module.addSerializer(Money.class, new MoneySerialize());
        objectMapper.registerModule(module);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        return objectMapper;
    }

    @Autowired(required =false)
    private HttpServletRequest request;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (int i = 0; i < converters.size(); i++) {
            if (converters.get(i) instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter() {

                    @Override
                    protected void writeInternal(Object e, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
                        if (e instanceof Map) {
                            super.writeInternal(e, type, outputMessage);
                            return;
                        }
                        if (request.getServletPath().startsWith("/swagger")) {
                            super.writeInternal(e, type, outputMessage);
                            return;
                        }
                        if (request.getServletPath().startsWith("/webjars")) {
                            super.writeInternal(e, type, outputMessage);
                            return;
                        }
                        if (request.getServletPath().startsWith("/api-docs")) {
                            super.writeInternal(e, type, outputMessage);
                            return;
                        }
                        if (request.getServletPath().startsWith("/v2")) {
                            super.writeInternal(e, type, outputMessage);
                            return;
                        }
                        Map<String, Object> map = new HashMap<>();
                        boolean seted = false;
                        if (e instanceof RPCResult) {
                            RPCResult result = (RPCResult) e;
                            if (result.getSuccess()) {
                                if (result.getTotalPage() != null && result.getTotalPage() > 0) {
                                    HashMap dataItem = new HashMap();
                                    dataItem.put("list", result.getData());
                                    dataItem.put("pageSize", Integer.valueOf(result.getPageSize()));
                                    dataItem.put("totalCount", Long.valueOf(result.getTotalCount()));
                                    dataItem.put("totalPage", Integer.valueOf(result.getTotalPage()));
                                    dataItem.put("pageIndex", Integer.valueOf(result.getPageIndex()));
                                    map.put("data", dataItem);
                                    map.put("success", result.getSuccess());
                                    super.writeInternal(map, type, outputMessage);
                                    return;
                                }
                                if (StringUtils.isNotBlank(result.getCode())) {
                                    map.put("code", result.getCode());
                                }
                                if (StringUtils.isNotBlank(result.getMessage())) {
                                    map.put("message", result.getMessage());
                                }
                                map.put("data", result.getData());
                                map.put("success", result.getSuccess());
                            } else {
                                map.put("code", result.getCode());
                                map.put("success", result.getSuccess());
                                map.put("message", Boolean.valueOf(false));
                            }
                            super.writeInternal(map, type, outputMessage);
                            return;
                        }
                        HashMap dataItem;
                        if (e instanceof List) {
                            seted = true;
                            dataItem = new HashMap();
                            dataItem.put("list", e);
                            map.put("data", dataItem);
                            map.put("success", Boolean.valueOf(true));
                            super.writeInternal(map, type, outputMessage);
                            return;
                        }
                        if (e instanceof Page) {
                            seted = true;
                            dataItem = new HashMap();
                            Page page = (Page) e;
                            dataItem.put("list", page.getContent());
                            dataItem.put("pageSize", Integer.valueOf(page.getSize()));
                            dataItem.put("totalCount", Long.valueOf(page.getTotalElements()));
                            dataItem.put("totalPage", Integer.valueOf(page.getTotalPages()));
                            dataItem.put("pageIndex", Integer.valueOf(page.getNumber()));
                            map.put("data", dataItem);
                            super.writeInternal(map, type, outputMessage);
                            return;
                        }
                        if (!seted) {
                            map.put("data", e);
                        }
                        map.put("success", Boolean.valueOf(true));
                        super.writeInternal(map, type, outputMessage);
                    }
                };
                converters.set(i, converter);
            }
        }
    }
}
