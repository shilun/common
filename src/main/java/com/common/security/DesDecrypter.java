//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.security;

import com.common.exception.ApplicationException;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class DesDecrypter extends Des {
    public static String decryptString(String str, String key) {
        try {
            DesDecrypter encrypter = new DesDecrypter(key);
            return encrypter.decrypt(str);
        }
        catch(Exception e){
            throw new ApplicationException("DesDecrypter.decryptString.error");
        }
    }

    DesDecrypter(String passPhrase) throws Exception {
        PBEKeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
        this.cipher = Cipher.getInstance(key.getAlgorithm());
        PBEParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
        this.cipher.init(2, key, paramSpec);
    }

    public String decrypt(String str) throws Exception {
        byte[] dec = Base32.decode(str);
        byte[] utf8 = this.decrypt(dec);
        return new String(utf8);
    }

    public byte[] decrypt(byte[] dec) throws IllegalBlockSizeException, BadPaddingException {
        return this.cipher.doFinal(dec);
    }

    public static void main(String[] args) throws Exception {
        String key = "";
        System.out.println("key = " + key);
        String encrypted = DesEncrypter.cryptString("hello world xxss", key);
        System.out.println("encrypted = " + encrypted);
        String plain = decryptString("ff0a9a7793994a43bafe62caef90f199:0424090daa0c417f99deed9ff5c21b8e", key);
        System.out.println("plain = " + plain);
    }
}
