//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.web;

import com.common.cookie.LoginContext;
import com.common.exception.BizException;
import com.common.util.LoginInfo;
import com.common.util.Money;
import com.common.util.RPCResult;
import com.common.util.Result;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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

    protected void tovm(Result<?> result) {
        Iterator var2 = result.keySet().iterator();

        while (var2.hasNext()) {
            String o = (String) var2.next();
            this.getRequest().setAttribute(o, result.get(o));
        }

    }

    protected void tovm(String key, Object value) {
        this.getRequest().setAttribute(key, value);
    }

    protected void tovm(Map<String, Object> result) {
        Iterator var2 = result.keySet().iterator();

        while (var2.hasNext()) {
            String o = (String) var2.next();
            this.getRequest().setAttribute(o, result.get(o));
        }

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

    protected String getCookeiValue(String key, String domain) {
        Cookie[] cookies = this.getRequest().getCookies();
        Cookie[] var4 = cookies;
        int var5 = cookies.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            Cookie cookie = var4[var6];
            if (cookie.getName().equalsIgnoreCase(key) && cookie.getDomain().equalsIgnoreCase(domain)) {
                return cookie.getValue();
            }
        }

        return null;
    }

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
}
