package com.common.mongo;


import org.springframework.core.convert.converter.Converter;

import java.util.ArrayList;
import java.util.List;

public class  MongConversionCfg {
    private static List<Converter> list = new ArrayList<Converter>();

    public static List<Converter> converList() {
        return list;
    }
}
