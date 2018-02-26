package com.common.mongo;

import com.common.util.Money;
import org.springframework.core.convert.converter.Converter;

public class LongToMoneyConvert implements Converter<Long,Money> {
@Override
public Money convert(Long source) {
        return new Money(source);
        }
}
