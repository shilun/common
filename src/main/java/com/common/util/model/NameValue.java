package com.common.util.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class NameValue {
    @ApiModelProperty("键")
    String name;
    @ApiModelProperty("值")
    String value;

    public NameValue() {
    }

    public NameValue(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
