package com.common.util;

import lombok.Data;

import java.io.Serializable;

/**
 * 抽像dto
 */
@Data
public abstract class AbstractDTO implements Serializable {
    private String id;
    private PageInfo pageinfo;
}
