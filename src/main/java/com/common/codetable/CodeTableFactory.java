package com.common.codetable;

import com.common.exception.BizException;
import com.common.util.IGlossary;
import com.common.util.MateDataInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shilun on 17-1-19.
 */
public class CodeTableFactory {
    private Map<String, MateDataInfo> types = new HashMap<>();


    public void addMateData(MateDataInfo mateDataInfo) {
        types.put(mateDataInfo.getCode(), mateDataInfo);
    }

    public MateDataInfo findByCode(String code) {
        return types.get(code);
    }

    /*扩展码表**/
    private List<ValuePare> codetables;

    /*枚举类型**/
    private List<ValuePare> glossaries;


    public List<ValuePare> getGlossaries() {
        return glossaries;
    }

    public void setGlossaries(List<ValuePare> glossaries) {
        this.glossaries = glossaries;
        try {
            for (ValuePare classStr : glossaries) {
                MateDataInfo mateDataInfo = new MateDataInfo();
                Class classz = Class.forName(classStr.getValue().toString());
                mateDataInfo.setName(classStr.getKey());
                mateDataInfo.setType(classz);
                mateDataInfo.setCode(classStr.getKey().toLowerCase());
                types.put(mateDataInfo.getCode(), mateDataInfo);
            }
        } catch (Exception e) {
            throw new BizException("set Glossaries error");
        }
    }

    public List<ValuePare> getCodetables() {
        return codetables;
    }

    public void setCodetables(List<ValuePare> codetables) {
        this.codetables = codetables;
        try {
            for (ValuePare value : codetables) {
                MateDataInfo mateDataInfo = new MateDataInfo();
                Class classz = Class.forName(value.getValue().toString());
                value.setValue(classz);
                mateDataInfo.setName(value.getKey());
                mateDataInfo.setType((Class) value.getValue());
                mateDataInfo.setCode(value.getKey().toLowerCase());
                types.put(mateDataInfo.getCode(), mateDataInfo);
            }
        } catch (Exception e) {
            throw new BizException("set Glossaries error");
        }
    }
}
