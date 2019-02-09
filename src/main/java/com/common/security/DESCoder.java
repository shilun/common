//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;

public abstract class DESCoder extends Coder {
    private static final Log log = LogFactory.getLog(DESCoder.class);
    public static final String ALGORITHM = "TripleDES";

    public DESCoder() {
    }

    private static Key toKey(byte[] key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key, "TripleDES");
        return secretKey;
    }

    public static byte[] decrypt(byte[] data, String key) throws Exception {
        Key k = toKey(decryptBASE64(key));
        Cipher cipher = Cipher.getInstance("TripleDES");
        cipher.init(2, k);
        return cipher.doFinal(data);
    }

    public static byte[] encrypt(byte[] data, String key) throws Exception {
        Key k = toKey(decryptBASE64(key));
        Cipher cipher = Cipher.getInstance("TripleDES");
        cipher.init(1, k);
        return cipher.doFinal(data);
    }

    public static String initKey() throws Exception {
        return initKey((String)null);
    }

    public static String initKey(String seed) throws Exception {
        SecureRandom secureRandom = null;
        if(seed != null) {
            secureRandom = new SecureRandom(decryptBASE64(seed));
        } else {
            secureRandom = new SecureRandom();
        }

        KeyGenerator kg = KeyGenerator.getInstance("TripleDES");
        kg.init(secureRandom);
        SecretKey secretKey = kg.generateKey();
        return encryptBASE64(secretKey.getEncoded());
    }
}
