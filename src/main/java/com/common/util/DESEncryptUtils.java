//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import com.common.exception.ApplicationException;
import com.sun.crypto.provider.SunJCE;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;
import java.security.Security;

public class DESEncryptUtils {
    private static final Logger logger = LoggerFactory.getLogger(DESEncryptUtils.class);
    private static String charsetName = "UTF-8";

    public DESEncryptUtils() {
    }

    public static String decrypt(String secretString, String secretKey) {
        if(!StringUtils.isEmpty(secretString) && !StringUtils.isEmpty(secretKey)) {
            try {
                byte[] e = Base64.decode(secretString);
                Security.addProvider(new SunJCE());
                SecureRandom sr = new SecureRandom();
                byte[] rawKeyData = (new String(secretKey)).getBytes(charsetName);
                DESKeySpec dks = new DESKeySpec(rawKeyData);
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
                SecretKey key = keyFactory.generateSecret(dks);
                Cipher cipher = Cipher.getInstance("DES");
                cipher.init(2, key, sr);
                byte[] decryptedData = cipher.doFinal(e);
                return new String(decryptedData, charsetName);
            } catch (Exception var10) {
                logger.error("operation failed. ", var10);
                return null;
            }
        } else {
            return null;
        }
    }

    public static String encrypt(String source, String secretKey) {
        if(!StringUtils.isEmpty(source) && !StringUtils.isEmpty(secretKey)) {
            try {
                byte[] e = source.getBytes(charsetName);
                Security.addProvider(new SunJCE());
                SecureRandom sr = new SecureRandom();
                byte[] rawKeyData = (new String(secretKey)).getBytes(charsetName);
                DESKeySpec dks = new DESKeySpec(rawKeyData);
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
                SecretKey key = keyFactory.generateSecret(dks);
                Cipher cipher = Cipher.getInstance("DES");
                cipher.init(1, key, sr);
                byte[] encryptData = cipher.doFinal(e);
                return Base64.encode(encryptData);
            } catch (Exception var10) {
                logger.error("operation failed. ", var10);
                throw new ApplicationException("加密出错", var10);
            }
        } else {
            return null;
        }
    }
}
