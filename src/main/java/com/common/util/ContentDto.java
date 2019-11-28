package com.common.util;

import lombok.Data;

import java.io.Serializable;
@Data
public class ContentDto implements Serializable {
    /**
     * 内容
     */
    private String content;
}
