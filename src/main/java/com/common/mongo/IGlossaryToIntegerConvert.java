package com.common.mongo;

import com.common.util.model.YesOrNoEnum;
import org.springframework.core.convert.converter.Converter;

public class IGlossaryToIntegerConvert implements Converter<YesOrNoEnum, Integer> {
    @Override
    public Integer convert(YesOrNoEnum source) {
        return source.getValue();
    }
}
