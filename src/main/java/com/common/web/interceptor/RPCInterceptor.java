//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.web.interceptor;

import com.common.util.DESEncryptUtils;
import com.common.util.DateUtil;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class RPCInterceptor implements HandlerInterceptor {
    private Logger logger = Logger.getLogger(RPCInterceptor.class);
    private String authKey;
    private boolean authHead = false;

    public RPCInterceptor() {
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader("authKey");
        if(this.authHead && StringUtils.isBlank(header)) {
            response.sendRedirect("/common/error?message=验证失败");
            return false;
        } else {
            if(this.authHead) {
                String decrypt = DESEncryptUtils.decrypt(header, this.authKey);
                String[] data = decrypt.split("_");
                if(data.length == 2 && data[0].equals(this.authKey)) {
                    Date remoteTime = DateUtil.parseDateTime(data[1]);
                    Date currentTime = Calendar.getInstance().getTime();
                    Date start = DateUtils.addMinutes(currentTime, -5);
                    Date end = DateUtils.addMinutes(currentTime, 5);
                    if(!remoteTime.after(start) || !remoteTime.before(end)) {
                        response.sendRedirect("/common/error?message=时间不准确请校对时间");
                        return false;
                    }
                }
            }

            return true;
        }
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public void setAuthHead(boolean authHead) {
        this.authHead = authHead;
    }
}
