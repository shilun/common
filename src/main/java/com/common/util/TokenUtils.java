package com.common.util;

import com.common.security.DesDecrypter;

/**
 * token 解密工具类
 */
public class TokenUtils {
    public static ILoginContext buildByToken(String token, String tokenKey) {

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
        };
        return context;
    }
}
