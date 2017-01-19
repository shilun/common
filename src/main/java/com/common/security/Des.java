//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.security;

import javax.crypto.Cipher;

public class Des {
    protected final String FACTORY_KEY = "PBEWithMD5AndDES";
    protected Cipher cipher;
    static byte[] salt = new byte[]{-87, -101, -56, 50, 86, 53, -29, 3};
    static int iterationCount = 19;

    public Des() {
    }
}
