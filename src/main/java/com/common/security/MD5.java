//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.security;

import java.lang.reflect.Array;
import org.apache.commons.codec.digest.DigestUtils;

public class MD5 {
    public MD5() {
    }

    public static String MD5Str(String str) {
        return DigestUtils.md5Hex(str);
    }

    public static String MD5Str(String content, String key) {
        return DigestUtils.md5Hex(content + key);
    }

    public static void main(String[] args) {
        MD5 m = new MD5();
        if(Array.getLength(args) == 0) {
            System.out.println("MD5 Test suite:");
            System.out.println("MD5(\"\"):" + MD5Str("admin26115be0be4d4d5caa1fb7cc067642fx"));
            System.out.println("MD5(\"a\"):" + MD5Str("a"));
            System.out.println("MD5(\"abc\"):" + MD5Str("abc"));
            System.out.println("MD5(\"message digest\"):" + MD5Str("message digest"));
            System.out.println("MD5(\"abcdefghijklmnopqrstuvwxyz\"):" + MD5Str("abcdefghijklmnopqrstuvwxyz"));
            System.out.println("MD5(\"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789\"):" + MD5Str("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"));
        } else {
            System.out.println("MD5(" + args[0] + ")=" + MD5Str(args[0]));
        }

    }
}
