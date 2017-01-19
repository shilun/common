//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.security;

class Util {
    public static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    Util() {
    }

    public static byte[] short2byte(short[] sa) {
        int length = sa.length;
        byte[] ba = new byte[length * 2];
        int i = 0;

        short k;
        for(int j = 0; i < length; ba[j++] = (byte)(k & 255)) {
            k = sa[i++];
            ba[j++] = (byte)(k >>> 8 & 255);
        }

        return ba;
    }

    public static short[] byte2short(byte[] ba) {
        int length = ba.length;
        short[] sa = new short[length / 2];
        int i = 0;

        for(int j = 0; j < length / 2; sa[j++] = (short)((ba[i++] & 255) << 8 | ba[i++] & 255)) {
            ;
        }

        return sa;
    }

    public static byte[] int2byte(int[] ia) {
        int length = ia.length;
        byte[] ba = new byte[length * 4];
        int i = 0;

        int k;
        for(int j = 0; i < length; ba[j++] = (byte)(k & 255)) {
            k = ia[i++];
            ba[j++] = (byte)(k >>> 24 & 255);
            ba[j++] = (byte)(k >>> 16 & 255);
            ba[j++] = (byte)(k >>> 8 & 255);
        }

        return ba;
    }

    public static int[] byte2int(byte[] ba) {
        int length = ba.length;
        int[] ia = new int[length / 4];
        int i = 0;

        for(int j = 0; j < length / 4; ia[j++] = (ba[i++] & 255) << 24 | (ba[i++] & 255) << 16 | (ba[i++] & 255) << 8 | ba[i++] & 255) {
            ;
        }

        return ia;
    }

    public static String toHEX(byte[] ba) {
        int length = ba.length;
        char[] buf = new char[length * 3];
        int i = 0;

        for(int j = 0; i < length; buf[j++] = 32) {
            byte k = ba[i++];
            buf[j++] = HEX_DIGITS[k >>> 4 & 15];
            buf[j++] = HEX_DIGITS[k & 15];
        }

        return new String(buf);
    }

    public static String toHEX(short[] ia) {
        int length = ia.length;
        char[] buf = new char[length * 5];
        int i = 0;

        for(int j = 0; i < length; buf[j++] = 32) {
            short k = ia[i++];
            buf[j++] = HEX_DIGITS[k >>> 12 & 15];
            buf[j++] = HEX_DIGITS[k >>> 8 & 15];
            buf[j++] = HEX_DIGITS[k >>> 4 & 15];
            buf[j++] = HEX_DIGITS[k & 15];
        }

        return new String(buf);
    }

    public static String toHEX(int[] ia) {
        int length = ia.length;
        char[] buf = new char[length * 10];
        int i = 0;

        for(int j = 0; i < length; buf[j++] = 32) {
            int k = ia[i++];
            buf[j++] = HEX_DIGITS[k >>> 28 & 15];
            buf[j++] = HEX_DIGITS[k >>> 24 & 15];
            buf[j++] = HEX_DIGITS[k >>> 20 & 15];
            buf[j++] = HEX_DIGITS[k >>> 16 & 15];
            buf[j++] = 32;
            buf[j++] = HEX_DIGITS[k >>> 12 & 15];
            buf[j++] = HEX_DIGITS[k >>> 8 & 15];
            buf[j++] = HEX_DIGITS[k >>> 4 & 15];
            buf[j++] = HEX_DIGITS[k & 15];
        }

        return new String(buf);
    }

    public static String toHEX1(byte b) {
        char[] buf = new char[2];
        byte j = 0;
        int var3 = j + 1;
        buf[j] = HEX_DIGITS[b >>> 4 & 15];
        buf[var3++] = HEX_DIGITS[b & 15];
        return new String(buf);
    }

    public static String toHEX1(byte[] ba) {
        int length = ba.length;
        char[] buf = new char[length * 2];
        int i = 0;

        byte k;
        for(int j = 0; i < length; buf[j++] = HEX_DIGITS[k & 15]) {
            k = ba[i++];
            buf[j++] = HEX_DIGITS[k >>> 4 & 15];
        }

        return new String(buf);
    }

    public static String toHEX1(short[] ia) {
        int length = ia.length;
        char[] buf = new char[length * 4];
        int i = 0;

        short k;
        for(int j = 0; i < length; buf[j++] = HEX_DIGITS[k & 15]) {
            k = ia[i++];
            buf[j++] = HEX_DIGITS[k >>> 12 & 15];
            buf[j++] = HEX_DIGITS[k >>> 8 & 15];
            buf[j++] = HEX_DIGITS[k >>> 4 & 15];
        }

        return new String(buf);
    }

    public static String toHEX1(int i) {
        char[] buf = new char[8];
        byte j = 0;
        int var3 = j + 1;
        buf[j] = HEX_DIGITS[i >>> 28 & 15];
        buf[var3++] = HEX_DIGITS[i >>> 24 & 15];
        buf[var3++] = HEX_DIGITS[i >>> 20 & 15];
        buf[var3++] = HEX_DIGITS[i >>> 16 & 15];
        buf[var3++] = HEX_DIGITS[i >>> 12 & 15];
        buf[var3++] = HEX_DIGITS[i >>> 8 & 15];
        buf[var3++] = HEX_DIGITS[i >>> 4 & 15];
        buf[var3++] = HEX_DIGITS[i & 15];
        return new String(buf);
    }

    public static String toHEX1(int[] ia) {
        int length = ia.length;
        char[] buf = new char[length * 8];
        int i = 0;

        int k;
        for(int j = 0; i < length; buf[j++] = HEX_DIGITS[k & 15]) {
            k = ia[i++];
            buf[j++] = HEX_DIGITS[k >>> 28 & 15];
            buf[j++] = HEX_DIGITS[k >>> 24 & 15];
            buf[j++] = HEX_DIGITS[k >>> 20 & 15];
            buf[j++] = HEX_DIGITS[k >>> 16 & 15];
            buf[j++] = HEX_DIGITS[k >>> 12 & 15];
            buf[j++] = HEX_DIGITS[k >>> 8 & 15];
            buf[j++] = HEX_DIGITS[k >>> 4 & 15];
        }

        return new String(buf);
    }

    public static byte[] hex2byte(String hex) {
        int len = hex.length();
        byte[] buf = new byte[(len + 1) / 2];
        int i = 0;
        int j = 0;
        if(len % 2 == 1) {
            buf[j++] = (byte)hexDigit(hex.charAt(i++));
        }

        while(i < len) {
            buf[j++] = (byte)(hexDigit(hex.charAt(i++)) << 4 | hexDigit(hex.charAt(i++)));
        }

        return buf;
    }

    public static boolean isHex(String hex) {
        int len = hex.length();
        int i = 0;

        char ch;
        do {
            do {
                do {
                    if(i >= len) {
                        return true;
                    }

                    ch = hex.charAt(i++);
                } while(ch >= 48 && ch <= 57);
            } while(ch >= 65 && ch <= 70);
        } while(ch >= 97 && ch <= 102);

        return false;
    }

    public static int hexDigit(char ch) {
        return ch >= 48 && ch <= 57?ch - 48:(ch >= 65 && ch <= 70?ch - 65 + 10:(ch >= 97 && ch <= 102?ch - 97 + 10:0));
    }

    public static void main(String[] args) {
        String s = toHEX1(new byte[]{10, 11, 12, 13});
        System.out.println("s = " + s);
    }
}
