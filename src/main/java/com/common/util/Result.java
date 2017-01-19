//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import com.common.exception.BizException;
import com.common.util.StringUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(
    name = "result",
    namespace = "com.neworder.common"
)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "result",
    namespace = "com.neworder.common"
)
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 6028636097083630372L;
    @XmlElement
    private T module;
    private List<T> items = new ArrayList();
    @XmlAttribute
    private Boolean success = Boolean.valueOf(false);
    @XmlAttribute
    private String resultCode;

    @XmlAttribute
    private String message;
    private Map<String, Object> result = new HashMap();
    public static final String DEFAULT_MODEL_KEY = "value";
    private String modelKey = "value";
    private String[] resultCodeParams;

    public List<T> getItems() {
        return this.items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result() {
    }

    public Object addDefaultModel(T obj) {
        if(obj instanceof List) {
            this.items.addAll((Collection)obj);
        } else {
            this.result.put("value", obj);
        }

        return this.module = obj;
    }

    public T getModule() {
        return this.module;
    }

    public void setModule(T module) {
        this.module = module;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Object addDefaultModel(String key, Object obj) {
        this.modelKey = key;
        return this.result.put(key, obj);
    }

    public Set<String> keySet() {
        return this.result.keySet();
    }

    public Object get() {
        return this.result.get(this.modelKey);
    }

    public Object get(String key) {
        return this.result.get(key);
    }

    public Collection values() {
        return this.result.values();
    }

    public Boolean getSuccess() {
        return this.success;
    }

    public Boolean isSuccess() {
        return this.success;
    }

    public String getResultCode() {
        return this.resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public void setResultCode(String resultCode, String... args) {
        this.resultCode = resultCode;
        this.resultCodeParams = args;
    }

    public String[] getResultCodeParams() {
        return this.resultCodeParams;
    }

    public void setResultCodeParams(String[] resultCodeParams) {
        this.resultCodeParams = resultCodeParams;
    }

    public void bind(BizException e) {
        this.setSuccess(Boolean.valueOf(false));
        this.setMessage(e.getMessage());
        if(StringUtils.isNotBlank(e.getCode())) {
            this.setResultCode(e.getCode());
        }

    }
}
