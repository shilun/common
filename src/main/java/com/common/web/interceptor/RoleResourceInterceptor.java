package com.common.web.interceptor;

import com.common.annotation.RoleResource;
import com.common.constants.GlobalContstants;
import com.common.cookie.LoginContext;
import com.common.util.Result;
import com.common.web.interceptor.rpc.ResourcesRPCService;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class RoleResourceInterceptor extends HandlerInterceptorAdapter {
    private static Logger logger = Logger.getLogger(RoleResourceInterceptor.class);
    private ResourcesRPCService resourcesRPCService;
    private String loginUrl;
    private Boolean audi = Boolean.valueOf(false);

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!this.audi.booleanValue()) {
            return true;
        } else if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            if (method != null && method.getMethodAnnotation(RoleResource.class) != null) {
                RoleResource methodAnnotation = (RoleResource) method.getMethodAnnotation(RoleResource.class);
                if (methodAnnotation == null) {
                    return true;
                } else if (LoginContext.getTicket() == null) {
                    response.sendRedirect(this.loginUrl);
                    return false;
                } else {
                    List resourceList = (List) request.getSession().getAttribute(GlobalContstants.LOGIN_ROLE_RESOURCE_KEY);
                    if (resourceList == null) {
                        Result<List<String>> roleResource = this.resourcesRPCService.findResources(LoginContext.getTicket().getLoginName());
                        if (!roleResource.getSuccess().booleanValue()) {
                            logger.error("操作员资源获取失败," + roleResource.getMessage());
                            return false;
                        }
                        resourceList = roleResource.getModule();
                        request.getSession().setAttribute(GlobalContstants.LOGIN_ROLE_RESOURCE_KEY, resourceList);
                    }
                    if (methodAnnotation != null && resourceList.contains(methodAnnotation.resource())) {
                        return true;
                    } else {
                        response.sendRedirect("/noright.html");
                        return false;
                    }
                }
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public void setResourcesRPCService(ResourcesRPCService resourcesRPCService) {
        this.resourcesRPCService = resourcesRPCService;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public void setAudi(Boolean audi) {
        this.audi = audi;
    }

}
