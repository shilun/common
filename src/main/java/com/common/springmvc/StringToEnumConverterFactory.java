package com.common.springmvc;

import com.common.context.Convertable;
import com.common.context.ConvertableContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

public final class StringToEnumConverterFactory implements ConverterFactory<String, Enum<?>> {
    private final ConversionService conversionService;

    public StringToEnumConverterFactory(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public <T extends Enum<?>> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnumConverterFactory.StringToEnum(this.conversionService, targetType);
    }

    static class StringToEnum<T extends Enum<T>> implements Converter<String, T> {
        private final ConversionService conversionService;
        private final Class<T> enumType;

        StringToEnum(ConversionService conversionService, Class<T> enumType) {
            this.conversionService = conversionService;
            this.enumType = enumType;
        }

        public T convert(String source) {
            if (!this.enumType.isAnnotationPresent(Convertable.class)) {
                return source != null && source.length() != 0 ? Enum.valueOf(this.enumType, source.trim()) : null;
            } else {
                ConvertableContext convertableContext = ConvertableContext.build(this.enumType);
                Object fromValue;
                if (source != null && source.length() != 0) {
                    fromValue = this.conversionService.convert(source, convertableContext.getValueType());
                } else {
                    fromValue = null;
                }

                try {
                    return this.enumType.cast(convertableContext.of(fromValue));
                } catch (Exception var5) {
                    throw new IllegalArgumentException(var5);
                }
            }
        }
    }
}
