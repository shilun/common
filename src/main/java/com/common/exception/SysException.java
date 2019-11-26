package com.common.exception;

/**
 * 系统异常
 */
public class SysException extends ApplicationException {

    public static SysException sysException;
    static {
        sysException=new SysException();
    }
    private SysException() {
    }

    @Override
    public String getCode() {
        return "system.error";
    }

    @Override
    public String getMessage() {
        return "系统错误";
    }
}
