package com.common.mongo;

import org.springframework.core.convert.converter.Converter;

public class IntegerToIGlossaryConvert implements Converter<Integer,Enum> {
    @Override
    public Enum convert(Integer source) {
        return null;
    }
}
