package com.common.codetable;

import com.common.util.MateDataInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shilun on 17-1-19.
 */
public class CodeTableFactory {
    private static Map<String, MateDataInfo> types = new HashMap<>();

    public static void addMateData(MateDataInfo mateDataInfo) {
        types.put(mateDataInfo.getCode(), mateDataInfo);
    }

    public static MateDataInfo findByCode(String code) {
        return types.get(code);
    }
}
