//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.security;

import com.common.security.Base32;
import com.common.security.Des;
import com.common.security.DesDecrypter;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class DesEncrypter extends Des {
    public static String cryptString(String str, String key) throws Exception {
        DesEncrypter encrypter = new DesEncrypter(key);
        return encrypter.encrypt(str);
    }

    public DesEncrypter(String passPhrase) throws Exception {
        PBEKeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
        this.cipher = Cipher.getInstance(key.getAlgorithm());
        PBEParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
        this.cipher.init(1, key, paramSpec);
    }

    public String encrypt(String str) throws Exception {
        byte[] utf8 = str.getBytes();
        byte[] enc = this.encrypt(utf8);
        return Base32.encode(enc);
    }

    public byte[] encrypt(byte[] utf8) throws IllegalBlockSizeException, BadPaddingException {
        return this.cipher.doFinal(utf8);
    }

    public static void main(String[] args) throws Exception {
        String key = "c2#sUjAKq3dGP7%Zjz-ydBPUvKoe_qI8";
        System.out.println("key = " + key);
        String encrypted = cryptString("hello world xxss", key);
        System.out.println("encrypted = " + encrypted);
        String plain = DesDecrypter.decryptString(encrypted, key);
        System.out.println("plain = " + plain);
    }
}
