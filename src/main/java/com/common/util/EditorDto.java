package com.common.util;


import com.common.util.model.EditorTypeEnum;

/**
 * Created by shilun on 17-1-6.
 */
public class EditorDto {
    /*排序**/
    private int seq;
    /*下拉列表标签×*/
    private boolean label;
    /*唯一**/
    private boolean value;
    /*名称**/
    private String name;

    /*是否在列表中现示**/
    private boolean list;

    /*类型**/
    private EditorTypeEnum type;

    /*必选**/
    private boolean request;

    /*最大值**/
    private int max;

    /*属性名称**/
    private String propertyName;

    /*数据提供器**/
    private String dataProvider;

    public boolean getLabel() {
        return label;
    }

    public void setLabel(boolean label) {
        this.label = label;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }


    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isList() {
        return list;
    }

    public void setList(boolean list) {
        this.list = list;
    }

    public EditorTypeEnum getType() {
        return type;
    }

    public void setType(EditorTypeEnum type) {
        this.type = type;
    }

    public boolean isRequest() {
        return request;
    }

    public void setRequest(boolean request) {
        this.request = request;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(String dataProvider) {
        this.dataProvider = dataProvider;
    }

}
