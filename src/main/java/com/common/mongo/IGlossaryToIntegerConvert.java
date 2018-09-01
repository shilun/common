package com.common.mongo;

import com.common.util.IGlossary;
import org.springframework.core.convert.converter.Converter;

public class IGlossaryToIntegerConvert implements Converter<Enum, Integer> {
    @Override
    public Integer convert(Enum source) {
        return ((IGlossary) source).getValue();
    }
}
