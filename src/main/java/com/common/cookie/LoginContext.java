//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.cookie;

import com.common.exception.ApplicationException;
import com.common.security.DesDecrypter;
import com.common.security.DesEncrypter;
import com.common.util.DateUtil;
import java.io.Serializable;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class LoginContext implements Serializable {
    private static final Logger logger = Logger.getLogger(LoginContext.class);
    private static final long serialVersionUID = 1L;
    private String loginName;
    private Date createTime;
    private long expires;

    public LoginContext() {
    }

    public void setTimeout(long timeout) {
        this.expires = this.createTime.getTime() + timeout;
    }

    public String toString() {
        if(StringUtils.isBlank(this.loginName)) {
            throw new ApplicationException("loginName can.t be blank");
        } else if(this.createTime == null) {
            throw new ApplicationException("loginTime can\'t be null");
        } else {
            StringBuilder content = new StringBuilder();
            content.append(this.loginName).append(",");
            content.append(DateUtil.formatDateTime(this.createTime, "yyyy-MM-dd HH:mm:ss SSS")).append(",");
            content.append(this.expires).append(",");
            return content.toString();
        }
    }

    public static LoginContext getTicket() {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        LoginContext attribute = (LoginContext)request.getSession().getAttribute("ticket");
        return attribute;
    }

    public static void setTicket(LoginContext ticket) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        request.getSession().setAttribute("ticket", ticket);
    }

    public static LoginContext getTicket(String content) {
        if(StringUtils.isBlank(content)) {
            return null;
        } else {
            String[] split = content.split(",");
            LoginContext info = new LoginContext();
            info.setLoginName(split[0]);

            try {
                info.setCreateTime(DateUtil.parse(split[1], "yyyy-MM-dd HH:mm:ss SSS"));
                return info;
            } catch (Exception var4) {
                logger.error("getToken  error ", var4);
                throw new ApplicationException("getToken error", var4);
            }
        }
    }

    public static LoginContext getTicket(String encrypted, String key) {
        String content = deCodeToken(encrypted, key);
        return getTicket(content);
    }

    public static String enCodeToken(LoginContext info, String key) {
        String encrypted = null;

        try {
            encrypted = DesEncrypter.cryptString(info.toString(), key);
            return encrypted;
        } catch (Exception var4) {
            logger.error("enCodeToken error", var4);
            throw new ApplicationException("enCodeToken error", var4);
        }
    }

    public static String deCodeToken(String encrypted, String key) {
        String plain = null;

        try {
            plain = DesDecrypter.decryptString(encrypted, key);
            return plain;
        } catch (Exception var4) {
            logger.error("deCodeToken error", var4);
            throw new ApplicationException("deCodeToken error", var4);
        }
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date loginTime) {
        this.createTime = loginTime;
    }

    public String getLoginName() {
        return this.loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public void setExpires(Long expires) {
        this.expires = expires.longValue();
    }

    public long getExpires() {
        return this.expires;
    }
}
