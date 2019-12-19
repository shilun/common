package com.common.config;

import com.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: cookie
 * @Date: 2018/7/26 15:09
 * @Description: 全局捕获异常和自定义全局捕获异常
 */
@ControllerAdvice
@Slf4j
public class GloablControllerAdvice {

    /**
     * 全局异常处理，反正异常返回统一格式的map
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = BizException.class)
    public Map<String, Object> bizExceptionHandler(BizException e) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", e.getCode());
        map.put("message", e.getMessage());
        map.put("success", Boolean.valueOf(false));
        log.error("execute json error->code:" + e.getCode() + " msg:" + e.getMessage());
        return map;
    }

    /**
     * 全局异常处理，反正异常返回统一格式的map
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Map<String, Object> exceptionHandler(Exception ex) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", "999");
        map.put("message", "执行业务失败");
        map.put("success", Boolean.valueOf(false));
        log.error("未知异常", ex);
        return map;
    }
}