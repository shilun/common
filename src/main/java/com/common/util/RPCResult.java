package com.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.domain.Page;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class RPCResult<T> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6028636097083630372L;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer totalCount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer totalPage;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer pageSize;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer pageIndex;


    /**
     * 是否成功
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean success = false;

    /**
     * 返回码
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String resultCode;
    /** 消息*/
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String[] resultCodeParams;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 默认构造方法
     */
    public RPCResult() {
    }

    /**
     * 默认构造方法
     */
    public RPCResult(Page page) {
        this.setTotalPage(page.getTotalPages());
        this.setPageSize(page.getSize());
        this.setTotalCount((int) page.getTotalElements());
        this.setPageIndex(page.getNumber());
        this.setData((T) page.getContent());
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }


    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public void setSuccess(Boolean success) {
        this.success = success;
    }


    /**
     * 返回是否成功
     *
     * @return
     */

    public Boolean getSuccess() {
        return success;
    }

    public Boolean isSuccess() {
        return success;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public void setResultCode(String resultCode, String... args) {
        this.resultCode = resultCode;
        this.resultCodeParams = args;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String[] getResultCodeParams() {
        return resultCodeParams;
    }

    public void setResultCodeParams(String[] resultCodeParams) {
        this.resultCodeParams = resultCodeParams;
    }


}
