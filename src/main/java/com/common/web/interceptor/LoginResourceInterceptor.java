//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.web.interceptor;

import com.common.annotation.LoginResource;
import com.common.cookie.LoginContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginResourceInterceptor extends HandlerInterceptorAdapter {
    private static Logger logger = LoggerFactory.getLogger(LoginResourceInterceptor.class);
    private Boolean audi = Boolean.valueOf(false);

    public LoginResourceInterceptor() {
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!this.audi.booleanValue()) {
            return true;
        } else {
            if(handler instanceof HandlerMethod) {
                HandlerMethod method = (HandlerMethod)handler;
                if(method == null || method.getMethodAnnotation(LoginResource.class) == null) {
                    return true;
                }

                LoginResource methodAnnotation = (LoginResource)method.getMethodAnnotation(LoginResource.class);
                if(methodAnnotation == null) {
                    return true;
                }

                if(LoginContext.getTicket() == null) {
                    return false;
                }
            }

            return true;
        }
    }

    public void setAudi(Boolean audi) {
        this.audi = audi;
    }
}
