package com.common.exception;

import lombok.Data;

/**
 * @Description: 自定义参数校验异常
 * @Date: 2019/1/24
 */
@Data
public class ParamsCheckException extends ApplicationException{

    private static final long serialVersionUID = 2684099760669375847L;

    /**
     * 异常编码
     */
    private String code;

    /**
     * 异常信息
     */
    private String message;

    public ParamsCheckException(){
        super();
    }

    public ParamsCheckException(String code, String message){
        this.code = code;
        this.message = message;
    }

    public ParamsCheckException(String message){
        this.message = message;
    }

}
