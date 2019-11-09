package com.common.util;

import lombok.Data;

import java.io.Serializable;


/**
 * 资源类型
 */
@Data
public class ResourceInfo implements Serializable {
    /**
     * 排序
     */
    private Integer seq;
    /**
     * 类型 1 图片 2 语音 3 视频
     */
    private Integer type;
    /**
     * 资源url
     */
    private String content;
}
