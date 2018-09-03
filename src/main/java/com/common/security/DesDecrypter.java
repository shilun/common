//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.security;

import com.common.exception.ApplicationException;
import com.common.security.Base32;
import com.common.security.Des;
import com.common.security.DesEncrypter;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
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
        String key = "ck|jhtr%oxo)ajlos\\qz=i_g,ge*g|j[";
        System.out.println("key = " + key);
        String encrypted = DesEncrypter.cryptString("hello world xxss", key);
        System.out.println("encrypted = " + encrypted);
        String plain = decryptString(encrypted, key);
        System.out.println("plain = " + plain);
    }
}
