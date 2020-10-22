
package com.common.util;

import com.common.annotation.QueryField;
import com.common.mongo.QueryType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Date;

@Data
public class AbstractBaseEntity implements Serializable {

    @Id
    private String id;
    private static final long serialVersionUID = 1L;
    @JsonIgnore
    @Transient
    private String orderColumn;
    @JsonIgnore
    @Transient
    private Integer startRow;
    @Transient
    @JsonIgnore
    private Integer endRow;
    private Date createTime;
    private Boolean delStatus;
    private Date updateTime;
    @Transient
    @JsonIgnore
    @QueryField(name = "id", type = QueryType.GTE)
    private Long minId;
    @Transient
    @QueryField(name = "createTime", type = QueryType.GTE)
    @JsonIgnore
    private Date startCreateTime;
    @QueryField(name = "createTime", type = QueryType.LTE)
    @Transient
    @JsonIgnore
    private Date endCreateTime;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.JSON_STYLE);
    }
}
