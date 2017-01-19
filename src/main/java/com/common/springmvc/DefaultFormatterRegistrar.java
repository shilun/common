package com.common.springmvc;

import org.springframework.core.convert.ConversionService;
import org.springframework.format.FormatterRegistrar;
import org.springframework.format.FormatterRegistry;

public class DefaultFormatterRegistrar implements FormatterRegistrar {
    public DefaultFormatterRegistrar() {
    }

    public void registerFormatters(FormatterRegistry registry) {
        if(registry instanceof ConversionService) {
            ConversionService service = (ConversionService)registry;
            registry.addConverterFactory(new StringToEnumConverterFactory(service));
        }

    }
}
