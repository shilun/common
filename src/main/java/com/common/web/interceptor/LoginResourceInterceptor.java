//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.web.interceptor;

import com.common.annotation.LoginResource;
import com.common.cookie.LoginContext;
import com.common.web.interceptor.rpc.ResourcesRPCService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginResourceInterceptor extends HandlerInterceptorAdapter {
    private static Logger logger = Logger.getLogger(LoginResourceInterceptor.class);
    private ResourcesRPCService operatorResourceRPCService;
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
