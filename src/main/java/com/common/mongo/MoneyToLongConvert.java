package com.common.mongo;

import com.common.util.Money;
import org.springframework.core.convert.converter.Converter;

public class MoneyToLongConvert implements Converter<Money,Long> {
@Override
public Long convert(Money source) {
        return source.getCent();
        }
}
