package com.common.config;

import com.alibaba.fastjson.JSONObject;
import com.common.exception.BizException;
import com.common.util.Money;
import com.common.util.RPCResult;
import com.common.util.StringUtils;
import com.common.web.CustomDateEditor;
import com.common.web.CustomMoneyEditor;
import com.common.web.CustomStringEditor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: cookie
 * @Date: 2018/7/26 15:09
 * @Description: 全局捕获异常和自定义全局捕获异常
 */
@ControllerAdvice
@Slf4j
public class GloablControllerAdvice implements ResponseBodyAdvice {

    @Autowired(required = false)
    private HttpServletRequest request;

    /**
     * 全局异常处理，反正异常返回统一格式的map
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public Map<String, Object> maxUploadSize(MaxUploadSizeExceededException e) {
        log.error("MaxUploadSizeExceededException:url->{}",request.getRequestURI());
        Map<String, Object> map = new HashMap<>();
        map.put("code", "file.upload.oversize.error");
        map.put("message", "文件上传,大小超限");
        map.put("success", Boolean.valueOf(false));
        log.error("execute json error->code:file.upload.oversize.error msg:" + e.getMessage());
        request.setAttribute("exception", true);
        return map;
    }
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> validationErrorHandler(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException:url->{}",request.getRequestURI());
        List<String> errorInformation = ex.getBindingResult().getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
        Map<String, Object> map = new HashMap<>();
        map.put("code", "parameter.error");
        map.put("message", errorInformation.get(0));
        map.put("success", Boolean.valueOf(false));

        return map;
    }
    /**
     * 全局异常处理，反正异常返回统一格式的map
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Map<String, Object> bizExceptionHandler(ConstraintViolationException e) {
        log.error("ConstraintViolationException:url->{}",request.getRequestURI());
        List<String> msgList = new ArrayList<>();
        for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
            Path propertyPath = constraintViolation.getPropertyPath();
            String name = null;
            if (propertyPath != null) {
                String pathStr = propertyPath.toString();
                int index = pathStr.indexOf(".");
                if (index != -1) {
                    name = pathStr.substring(index + 1);
                }
            }
            msgList.add("参数->" + StringUtils.defaultIfBlank(name, "")  + constraintViolation.getMessage());
        }
        String messages = StringUtils.join(msgList.toArray(), ";");
        Map<String, Object> map = new HashMap<>();
        map.put("code", "Violation.error");
        map.put("message", messages);
        map.put("success", Boolean.valueOf(false));
        log.error("验证错误",e);
        request.setAttribute("exception", true);
        return map;
    }

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
        log.error("BizException:url->{}",request.getRequestURI());
        log.error("execute json error->code:" + e.getCode() + " msg:" + e.getMessage());
        request.setAttribute("exception", true);
        return map;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(true));
        binder.registerCustomEditor(String.class, new CustomStringEditor());
        binder.registerCustomEditor(Money.class, new CustomMoneyEditor());
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
        if (ex instanceof DuplicateKeyException) {
            request.setAttribute("exception", true);
            Map<String, Object> map = new HashMap<>();
            map.put("code", "duplicat");
            map.put("message", "数据已经存在");
            map.put("success", Boolean.valueOf(false));
            log.error("数据重复", ex);
            return map;
        }
        request.setAttribute("exception", true);
        Map<String, Object> map = new HashMap<>();
        map.put("code", "999");
        map.put("message", "执行业务失败");
        map.put("success", Boolean.valueOf(false));
        log.error("未知异常", ex);
        return map;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object e, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest req, ServerHttpResponse response) {

        ResponseBody method = returnType.getMethod().getAnnotation(ResponseBody.class);
        if (method != null) {
            return e;
        }
        if (e instanceof Map) {
            if (request.getAttribute("buildMessage") != null) {
                return e;
            }
        }
        if (request.getAttribute("exception") != null) {
            return e;
        }
        if (request.getServletPath().startsWith("/swagger")) {
            return e;
        }
        if (request.getServletPath().startsWith("/webjars")) {
            return e;
        }
        if (request.getServletPath().startsWith("/api-docs")) {
            return e;
        }
        if (request.getServletPath().startsWith("/v2")) {
            return e;
        }

        Map<String, Object> map = new HashMap<>();
        if (e instanceof RPCResult) {
            RPCResult result = (RPCResult) e;
            if (result.getSuccess()) {
                if (result.getTotalPage() != null && result.getTotalPage() != null) {
                    HashMap dataItem = new HashMap();
                    dataItem.put("list", result.getData());
                    dataItem.put("pageSize", Integer.valueOf(result.getPageSize()));
                    dataItem.put("totalCount", Long.valueOf(result.getTotalCount()));
                    dataItem.put("totalPage", Integer.valueOf(result.getTotalPage()));
                    dataItem.put("pageIndex", Integer.valueOf(result.getPageIndex()));
                    map.put("data", dataItem);
                    map.put("success", result.getSuccess());
                    return map;
                }
                if (StringUtils.isNotBlank(result.getCode())) {
                    map.put("code", result.getCode());
                }
                if (StringUtils.isNotBlank(result.getMessage())) {
                    map.put("message", result.getMessage());
                }
                map.put("data", result.getData());
                map.put("success", result.getSuccess());
            } else {
                map.put("code", result.getCode());
                map.put("success", result.getSuccess());
                map.put("message", Boolean.valueOf(false));
            }
            return map;
        }
        HashMap dataItem;
        if (e instanceof List) {
            dataItem = new HashMap();
            dataItem.put("list", e);
            map.put("data", dataItem);
            map.put("success", Boolean.valueOf(true));
            return map;
        }
        if (e instanceof Page) {
            dataItem = new HashMap();
            Page page = (Page) e;
            dataItem.put("list", page.getContent());
            dataItem.put("pageSize", Integer.valueOf(page.getSize()));
            dataItem.put("totalCount", Long.valueOf(page.getTotalElements()));
            dataItem.put("totalPage", Integer.valueOf(page.getTotalPages()));
            dataItem.put("pageIndex", Integer.valueOf(page.getNumber()));
            map.put("success", Boolean.valueOf(true));
            map.put("data", dataItem);
            return map;
        }
        if (e != null) {
            map.put("data", e);
        }
        map.put("success", Boolean.valueOf(true));
        if (e instanceof String) {
            return new JSONObject(map).toString();
        }
        return map;
    }
}
