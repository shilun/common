package com.common.mongo;

import com.common.util.GlosseryEnumUtils;
import com.common.util.model.YesOrNoEnum;
import org.springframework.core.convert.converter.Converter;

public class IntegerToIGlossaryConvert implements Converter<Integer, YesOrNoEnum> {
    @Override
    public YesOrNoEnum convert(Integer source) {
        return GlosseryEnumUtils.getItem(YesOrNoEnum.class,source);
    }
}
