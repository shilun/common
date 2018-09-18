//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.web;

import com.common.cookie.LoginContext;
import com.common.exception.ApplicationException;
import com.common.exception.BizException;
import com.common.security.DesDecrypter;
import com.common.security.DesEncrypter;
import com.common.util.*;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public abstract class AbstractController {
    private static final Logger LOGGER = Logger.getLogger(AbstractController.class);

    public AbstractController() {
    }

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(true));
        binder.registerCustomEditor(String.class, new CustomStringEditor());
        binder.registerCustomEditor(Money.class, new CustomMoneyEditor());
    }

    protected Map<String, Object> buildMessage(IExecute execute) {
        HashMap map = new HashMap();

        try {
            Object e = execute.getData();
            if (e != null) {
                boolean seted = false;
                if (!seted && e instanceof Result) {
                    Result dataItem1 = (Result) e;
                    map.put("success", dataItem1.getSuccess());
                    HashMap page1;
                    if (!dataItem1.getSuccess().booleanValue()) {
                        page1 = new HashMap();
                        page1.put("code", dataItem1.getResultCode());
                        map.put("message", dataItem1.getMessage());
                        return map;
                    }

                    page1 = new HashMap();
                    Set keySet = dataItem1.keySet();
                    Iterator var8 = keySet.iterator();

                    while (var8.hasNext()) {
                        String key = (String) var8.next();
                        Object value = dataItem1.get(key);
                        if (value instanceof Page) {
                            Page pateItem = (Page) value;
                            HashMap pageValue = new HashMap();
                            pageValue.put("result", pateItem.getContent());
                            pageValue.put("total", pateItem.getTotalElements());
                            page1.put(key, pageValue);
                        } else {
                            page1.put(key, value);
                        }
                    }

                    map.put("data", page1);
                    seted = true;
                    return map;
                }

                HashMap dataItem;
                if (e instanceof List) {
                    seted = true;
                    dataItem = new HashMap();
                    dataItem.put("list", e);
                    map.put("data", dataItem);
                    map.put("success", Boolean.valueOf(true));
                    return map;
                }

                if (e instanceof Page) {
                    seted = true;
                    dataItem = new HashMap();
                    Page page = (Page) e;
                    dataItem.put("list", page.getContent());
                    dataItem.put("pageSize", Integer.valueOf(page.getSize()));
                    dataItem.put("totalCount", Long.valueOf(page.getTotalElements()));
                    dataItem.put("totalPage", Integer.valueOf(page.getTotalPages()));
                    dataItem.put("pageIndex", Integer.valueOf(page.getNumber()));
                    map.put("data", dataItem);
                }

                if (!seted) {
                    map.put("data", e);
                }
            }

            map.put("success", Boolean.valueOf(true));
        } catch (BizException var13) {
            LOGGER.error(var13.getMessage(), var13);
            LOGGER.error("execute json error->" + var13.getMessage());
            map.put("code", var13.getCode());
            map.put("message", var13.getMessage());
            map.put("success", Boolean.valueOf(false));
        } catch (Exception var14) {
            LOGGER.error(var14.getMessage(), var14);
            LOGGER.error("execute json error->" + var14.getMessage());
            map.put("code", "999");
            map.put("message", "执行业务失败");
            map.put("success", Boolean.valueOf(false));
        }

        return map;
    }

    protected <T> RPCResult<T> buildRPCMessage(IExecute execute) {
        RPCResult<T> result = new RPCResult<>();

        try {
            Object e = execute.getData();
            if (e != null) {
                boolean seted = false;
                if (!seted && e instanceof RPCResult) {
                    RPCResult dataItem1 = (RPCResult) e;
                    result.setSuccess(true);
                    return dataItem1;
                }

                if (e instanceof List) {
                    result.setData((T) e);
                    result.setSuccess(true);
                    return result;
                }
                if (e instanceof Page) {
                    Page page = (Page) e;
                    result.setData((T) ((Page) e).getContent());
                    result.setTotalCount(Long.valueOf(page.getTotalElements()).intValue());
                    result.setTotalPage(Integer.valueOf(page.getSize()));
                    result.setPageIndex(Integer.valueOf(page.getNumber()));
                    result.setSuccess(true);
                    return result;
                }
                if (e instanceof RPCResult) {

                    RPCResult resultRpc = (RPCResult) e;
                    if(resultRpc.getSuccess()){
                        result.setSuccess(true);
                        if (resultRpc.getTotalPage() != null && resultRpc.getTotalPage().intValue() > 0) {
                            result.setData((T) resultRpc.getData());
                            result.setTotalCount(resultRpc.getTotalCount());
                            result.setTotalPage(resultRpc.getTotalPage());
                            result.setPageIndex(resultRpc.getPageIndex());
                            return result;
                        }
                        else{
                            result.setData((T) resultRpc.getData());

                        }
                        return result;
                    }
                    else {
                        result.setCode(resultRpc.getCode());
                        result.setMessage(resultRpc.getMessage());
                        result.setSuccess(false);
                        return result;
                    }
                }
                if (e != null) {
                    result.setData((T) e);
                    result.setSuccess(true);
                    return result;
                }

            }
            result.setSuccess(true);
            return result;
        } catch (BizException biz) {
            LOGGER.error("remote call error:code->" + biz.getCode() + " message->" + biz.getMessage());
            LOGGER.error("execute json error->" + biz.getMessage());
            result.setCode(biz.getCode());
            result.setMessage(biz.getMessage());
            result.setSuccess(false);
            return result;
        } catch (Exception var14) {
            LOGGER.error(var14.getMessage(), var14);
            LOGGER.error("execute json error->" + var14.getMessage());
            result.setCode("999");
            result.setMessage("未知错误");
            result.setSuccess(false);
            return result;
        }
    }

    protected HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }


    /**
     * 获取加密的cookie
     *
     * @param cookieKey
     * @param encodeKey
     * @return
     */
    protected String getCookie(String cookieKey, String encodeKey) {
        String cookeiValue = getCookeiValue(cookieKey);
        return DesDecrypter.decryptString(cookeiValue, encodeKey);
    }

    /**
     * 获取cookie
     *
     * @param key
     * @return
     */
    protected String getCookeiValue(String key) {
        Cookie[] cookies = this.getRequest().getCookies();
        if (cookies == null) {
            return null;
        } else {
            Cookie[] var3 = cookies;
            int var4 = cookies.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                Cookie cookie = var3[var5];
                if (cookie.getName().equalsIgnoreCase(key)) {
                    return cookie.getValue();
                }
            }
            return null;
        }
    }

    /**
     * 设置cookie
     *
     * @param name
     * @param value
     */
    protected void putCookie(String name, String value, HttpServletResponse response) {
        String domain = StringUtils.getDomain(getRequest().getRequestURL().toString());
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(domain);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 设置cookie
     *
     * @param name
     * @param value
     * @param encodeKey
     */
    protected void putCookie(String name, String value, String encodeKey, HttpServletResponse response) {
        String domain = StringUtils.getDomain(getRequest().getRequestURL().toString());
        value=DesEncrypter.cryptString(value, encodeKey);
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(domain);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public LoginInfo getLogin() {
        LoginContext ticket = LoginContext.getTicket();
        if (ticket == null) {
            return null;
        } else {
            LoginInfo info = new LoginInfo();
            info.setLoginName(ticket.getLoginName());
            return info;
        }
    }

    protected Map<String, String> ListParameter(HttpServletRequest request) {
        HashMap params = new HashMap();
        Map requestParams = request.getParameterMap();
        Iterator iter = requestParams.keySet().iterator();
        while (iter.hasNext()) {
            String name = (String) iter.next();
            String[] values = (String[]) ((String[]) requestParams.get(name));
            String valueStr = "";
            for (int i = 0; i < values.length; ++i) {
                valueStr = i == values.length - 1 ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        return params;
    }


    /**
     * 获取JSON id
     *
     * @return
     */
    protected Long getIdByJson(String content) {
        try {
            JSONObject json = JSONObject.fromObject(content);
            return json.getLong("id");
        } catch (Exception e) {
            LOGGER.error("get json id error", e);
        }
        throw new ApplicationException("获取JSON id 失败");
    }
}
