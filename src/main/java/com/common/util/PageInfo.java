package com.common.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;

@ApiModel("分页参数")
@Data
public class PageInfo implements Serializable {
    @ApiModelProperty("页码")
    private Integer page;
    @ApiModelProperty("页大小")
    private Integer size;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    public Pageable getPageable() {
        if (page == null) {
            page = 0;
        }
        if (size == null || size.intValue() == 0) {
            size = 10;
        }
        return PageRequest.of(page, size);
    }
}
