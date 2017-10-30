//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import com.common.annotation.GeneratedValue;
import com.common.annotation.QueryField;
import com.common.mongo.QueryType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

public class AbstractBaseEntity implements Serializable {

    @Id
    @JsonIgnore
    private String uuid;
    private static final long serialVersionUID = 1L;
    @JsonIgnore
    private String orderColumn;
    @JsonIgnore
    private Integer startRow;
    @JsonIgnore
    private Integer endRow;
    @QueryField
    @GeneratedValue
    private Long id;
    @JsonIgnore
    private Date createTime;
    @JsonIgnore
    private Integer delStatus;
//    @JsonIgnore
    private Date updateTime;
    @JsonIgnore
    private Long minId;
    @JsonIgnore
    @QueryField(name="createTime",type = QueryType.GTE )
    private Date startCreateTime;
    @JsonIgnore
    @QueryField(name="createTime",type = QueryType.LTE )
    private Date endCreateTime;
    @JsonIgnore
    private Integer orderTpe;//1 升序ASC，2 降序Desc

    public Long getMinId() {
        return this.minId;
    }

    public void setMinId(Long minId) {
        this.minId = minId;
    }

    public Date getStartCreateTime() {
        return this.startCreateTime;
    }

    public void setStartCreateTime(Date startCreateTime) {
        this.startCreateTime = startCreateTime;
    }

    public Date getEndCreateTime() {
        return this.endCreateTime;
    }

    public void setEndCreateTime(Date endCreateTime) {
        this.endCreateTime = endCreateTime;
    }

    public AbstractBaseEntity() {
    }

    public String getOrderColumn() {
        return this.orderColumn;
    }

    public void setOrderColumn(String orderColumn) {
        this.orderColumn = orderColumn;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStartRow() {
        return this.startRow;
    }

    public void setStartRow(Integer startRow) {
        this.startRow = startRow;
    }

    public Integer getEndRow() {
        return this.endRow;
    }

    public void setEndRow(Integer endRow) {
        this.endRow = endRow;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getDelStatus() {
        return this.delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getOrderTpe() {
        return orderTpe;
    }

    public void setOrderTpe(Integer orderTpe) {
        this.orderTpe = orderTpe;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.MULTI_LINE_STYLE);
    }
}
