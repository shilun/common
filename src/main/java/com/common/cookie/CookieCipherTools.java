//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.cookie;

import com.common.security.Base32;
import com.common.security.DESCoder;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.UnsupportedEncodingException;

public class CookieCipherTools {
    private static final Log log = LogFactory.getLog(CookieCipherTools.class);
    private String charsetName;

    public CookieCipherTools() {
    }

    public String encrypt(String value, String key) {
        try {
            byte[] e;
            if(!StringUtils.isEmpty(this.charsetName)) {
                try {
                    e = value.getBytes(this.charsetName);
                } catch (Exception var5) {
                    log.error("charset " + this.charsetName + " Unsupported!", var5);
                    e = value.getBytes();
                }
            } else {
                e = value.getBytes();
            }

            byte[] bytes = this.encrypt(key, e);
            return this.encoding(bytes);
        } catch (Exception var6) {
            log.error("encrypt error", var6);
            return null;
        }
    }

    private String encoding(byte[] bytes) throws Exception {
        return Base32.encode(bytes);
    }

    private byte[] decoding(String value) throws Exception {
        return Base32.decode(value);
    }

    private byte[] encrypt(String key, byte[] data) throws Exception {
        return DESCoder.encrypt(data, key);
    }

    private byte[] decrypt(String key, byte[] data) throws Exception {
        return DESCoder.decrypt(data, key);
    }

    public String decrypt(String value, String key) {
        try {
            byte[] e = this.decoding(value);
            byte[] bytes = this.decrypt(key, e);
            if(!StringUtils.isEmpty(this.charsetName)) {
                try {
                    return new String(bytes, this.charsetName);
                } catch (UnsupportedEncodingException var6) {
                    log.error("charset " + this.charsetName + " Unsupported!", var6);
                    return new String(bytes);
                }
            } else {
                return new String(bytes);
            }
        } catch (Exception var7) {
            log.error("encrypt error", var7);
            return null;
        }
    }

    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }
}
