package com.common.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.common.fastxml.MoneySerialize;
import com.common.util.Money;
import com.common.util.RPCResult;
import com.common.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter() {
            @Override
            public void write(Object o, Type type, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
                super.writeInternal(o, outputMessage);
            }
            protected void writeInternal(Object e, HttpOutputMessage outputMessage) throws IOException {
                HashMap map = new HashMap();
                boolean seted = false;
                if (e instanceof Map) {
                    super.writeInternal(e, outputMessage);
                } else {
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
                                super.writeInternal(map, outputMessage);
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
                        super.writeInternal(map, outputMessage);
                        return;
                    }
                    HashMap dataItem;
                    if (e instanceof List) {
                        seted = true;
                        dataItem = new HashMap();
                        dataItem.put("list", e);
                        map.put("data", dataItem);
                        map.put("success", Boolean.valueOf(true));
                        super.writeInternal(map, outputMessage);
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
                        super.writeInternal(map, outputMessage);
                        return;
                    }
                    if (!seted) {
                        map.put("data", e);
                    }
                    map.put("success", Boolean.valueOf(true));
                    super.writeInternal(map, outputMessage);
                }
            }
        };
        for (int i = 0; i < converters.size(); i++) {
            if (converters.get(i) instanceof MappingJackson2HttpMessageConverter) {
                converters.set(i, fastConverter);
            }
        }
    }
}
