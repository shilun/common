package com.common.config;

import com.common.fastxml.MoneySerialize;
import com.common.util.DateUtil;
import com.common.util.Money;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Configuration
@Slf4j
public class CommonConfig implements WebMvcConfigurer {

    @Value("${server.port}")
    private Integer serverPort;


    @Bean
    public ConfigurableServletWebServerFactory configurableServletWebServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.setPort(serverPort);
        factory.setBaseDirectory(new File("/tomcat"));
        return factory;
    }


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter item = (MappingJackson2HttpMessageConverter) converter;
                ObjectMapper objectMapper = item.getObjectMapper();
                objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

                SimpleModule moneyModule = new SimpleModule();
                moneyModule.addSerializer(Money.class, new MoneySerialize());
                moneyModule.addDeserializer(Money.class, new JsonDeserializer() {
                    @Override
                    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
                        String text = jsonParser.getText();
                        BigDecimal bigDecimal = NumberUtils.createBigDecimal(text);
                        return new Money(bigDecimal);
                    }
                });
                moneyModule.addSerializer(Date.class, new JsonSerializer<Date>() {
                    @Override
                    public void serialize(Date money, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
                        jsonGenerator.writeString(DateUtil.format(money));
                    }
                });
                moneyModule.addDeserializer(Date.class, new JsonDeserializer<Date>() {
                    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    private SimpleDateFormat dateFormatFirst = new SimpleDateFormat("yyyy.MM.dd");
                    private SimpleDateFormat chDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
                    private SimpleDateFormat minuteDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    private SimpleDateFormat hourDateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
                    private SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");

                    @Override
                    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
                        String text = jsonParser.getText();
                        if (StringUtils.isEmpty(text)) {
                            return null;
                        }
                        try {
                            if (text.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
                                return dateFormat.parse(text);
                            }
                            if (text.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}")) {
                                return minuteDateFormat.parse(text);
                            }
                            if (text.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}")) {
                                return hourDateFormat.parse(text);
                            }
                            if (text.matches("\\d{4}\\.\\d{2}\\.\\d{2}")) {
                                return dateFormatFirst.parse(text);
                            }
                            if (text.matches("\\d{4}年\\d{2}月\\d{2}日")) {
                                return chDateFormat.parse(text);
                            }
                            return DateUtil.parseDate(text);
                        } catch (ParseException var3) {
                            throw new IllegalArgumentException("Could not parse date: " + var3.getMessage(), var3);
                        }
                    }
                });
                objectMapper.registerModule(moneyModule);
            }
        }
    }

    public static void main(String[] args) {
        int dayOfWeek = new DateTime(DateUtil.parseDate("02-16")).getDayOfWeek();
        System.out.println(dayOfWeek);
    }
}

