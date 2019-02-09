//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.web.interceptor;

import com.common.cookie.CookieUtils;
import com.common.cookie.LoginContext;
import com.common.util.StringUtils;
import com.common.web.url.JdUrl;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.util.Date;

public class LoginContextInterceptor extends HandlerInterceptorAdapter {
    private static final Log log = LogFactory.getLog(LoginContextInterceptor.class);
    @Autowired(required=false)
    protected CookieUtils cookieUtils;
    protected String loginCookieKey;
    protected int rate = 2;
    protected int sessionTimeout = 1800;
    private String loginUrl;
    private JdUrl homeModule;

    public LoginContextInterceptor() {
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!(handler instanceof HandlerMethod)) {
            return true;
        } else {
            LoginContext ticket = LoginContext.getTicket();
            if(ticket == null) {
                if(this.loginCookieKey == null) {
                    log.debug("session cookie key is empty!");
                    response.sendRedirect(this.getLoginUrl());
                    return false;
                }

                try {
                    String var11 = this.cookieUtils.getCookieValue(request, this.loginCookieKey);
                    if(!StringUtils.isNotBlank(var11)) {
                        log.debug("session cookie[" + this.loginCookieKey + "] is empty!");
                        response.sendRedirect(this.getLoginUrl());
                        return false;
                    }

                    LoginContext context = this.getLoginContext(var11);
                    if(context == null) {
                        log.debug("session cookie[" + this.loginCookieKey + "] is error!");
                        response.sendRedirect(this.getLoginUrl());
                        return false;
                    }

                    Date minDateTime = context.getCreateTime();
                    Date maxCreateTime = DateUtils.addMinutes(minDateTime, 40);
                    Date currentDate = new Date();
                    boolean cookieOk = false;
                    if(currentDate.before(maxCreateTime)) {
                        context.setCreateTime(currentDate);
                        this.cookieUtils.setCookie(response, this.loginCookieKey, context.toString());
                        cookieOk = true;
                    }

                    if(!cookieOk) {
                        response.sendRedirect(this.getLoginUrl());
                        return false;
                    }

                    LoginContext.setTicket(context);
                } catch (Exception var111) {
                    log.error("login intercept error", var111);
                }
            }

            return true;
        }
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    private String getLoginUrl() throws MalformedURLException {
        HttpServletRequest request = this.getRequest();
        JdUrl url = new JdUrl();
        url.setUrl(this.loginUrl);
        String queryString = request.getQueryString();
        queryString = StringUtils.defaultString(queryString, "");
        if(StringUtils.isNotBlank(queryString)) {
            queryString = "?" + queryString;
        }

        url.addQueryData("returnUrl", "http://" + request.getHeader("host") + StringUtils.defaultIfBlank(request.getPathInfo(),"") + queryString);
        return url.toString();
    }

    protected HttpServletRequest getRequest() {
        return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
    }

    protected LoginContext getLoginContext(String cookieValue) {
        return LoginContext.getTicket(cookieValue);
    }

    public void setCookieUtils(CookieUtils cookieUtils) {
        this.cookieUtils = cookieUtils;
    }

    public void setLoginCookieKey(String loginCookieKey) {
        this.loginCookieKey = loginCookieKey;
    }

    public void setHomeModule(JdUrl homeModule) {
        this.homeModule = homeModule;
    }
}
