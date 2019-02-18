package com.common.util;

import com.common.exception.ApplicationException;
import com.common.security.DesDecrypter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * token 解密工具类
 */
public class TokenUtils {
    private final static Logger logger = LoggerFactory.getLogger(TokenUtils.class);

    public static ILoginContext buildByToken(String token, String tokenKey) {
        try {
            token = DesDecrypter.decryptString(token, tokenKey);
            String[] items = token.split(":");

            ILoginContext context = new ILoginContext() {
                @Override
                public Long getProxyId() {
                    return Long.parseLong(items[0]);
                }

                @Override
                public String getPin() {
                    return items[1];
                }

                @Override
                public String getToken() {
                    return items[2];
                }
            };
            return context;
        } catch (Exception e) {
            logger.error("token.decrypt.error:token="+token+",tokenKey="+tokenKey);
        }
        throw new ApplicationException("token.decrypt.error");
    }

}
