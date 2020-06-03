package com.common.util;

import com.common.exception.BizException;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;


@Slf4j
public class RPCResult<T> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6028636097083630372L;
    private Integer totalCount;
    private Integer totalPage;
    private Integer pageSize;
    private Integer pageIndex;


    /**
     * 是否成功
     */
    private Boolean success = false;

    /**
     * 返回码
     */
    private String resultCode;
    /**
     * 消息
     */
    private String message;
    private String code;
    private String[] resultCodeParams;
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.setSuccess(true);
        if (this.data instanceof Page) {
            Page page = (Page) data;
            this.setTotalPage(page.getTotalPages());
            this.setPageSize(page.getSize());
            this.setTotalCount((int) page.getTotalElements());
            this.setPageIndex(page.getNumber());
            this.setData((T) page.getContent());
            return;
        }

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

    public Page<T> toPage() {
        Pageable pageable = null;
        List<T> list = (List<T>) data;
        if (this.totalPage > 1) {
            pageable = PageRequest.of(this.pageIndex, this.pageSize);
            return new PageImpl(list, pageable, this.getTotalCount());
        } else {
            return new PageImpl(list);
        }

    }

    /**
     * 默认构造方法
     */
    public RPCResult(T data) {
        this.setSuccess(true);
        this.setData(data);
    }

    /**
     * 默认构造方法
     */
    public RPCResult(Exception e) {
        if (e instanceof BizException) {
            this.setException((BizException) e);
        } else {
            log.error("unKnow.error", e);
            this.setCode("999");
            this.setMessage("执行业务失败");
        }
        this.setSuccess(false);

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
        this.setSuccess(true);
    }

    public RPCResult(BizException exception) {
        this.message = exception.getMessage();
        this.code = exception.getCode();
        this.setSuccess(false);
    }

    public void setException(BizException exception) {
        this.message = exception.getMessage();
        this.code = exception.getCode();
        this.setSuccess(false);
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
