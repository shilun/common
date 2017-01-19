//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.security;

import com.common.security.Util;
import java.util.Arrays;

public class AES {
    public int traceLevel = 0;
    public String traceInfo = "";
    public static final int ROUNDS = 14;
    public static final int BLOCK_SIZE = 16;
    public static final int KEY_LENGTH = 32;
    int numRounds;
    byte[][] Ke;
    byte[][] Kd;
    static final byte[] S = new byte[]{99, 124, 119, 123, -14, 107, 111, -59, 48, 1, 103, 43, -2, -41, -85, 118, -54, -126, -55, 125, -6, 89, 71, -16, -83, -44, -94, -81, -100, -92, 114, -64, -73, -3, -109, 38, 54, 63, -9, -52, 52, -91, -27, -15, 113, -40, 49, 21, 4, -57, 35, -61, 24, -106, 5, -102, 7, 18, -128, -30, -21, 39, -78, 117, 9, -125, 44, 26, 27, 110, 90, -96, 82, 59, -42, -77, 41, -29, 47, -124, 83, -47, 0, -19, 32, -4, -79, 91, 106, -53, -66, 57, 74, 76, 88, -49, -48, -17, -86, -5, 67, 77, 51, -123, 69, -7, 2, 127, 80, 60, -97, -88, 81, -93, 64, -113, -110, -99, 56, -11, -68, -74, -38, 33, 16, -1, -13, -46, -51, 12, 19, -20, 95, -105, 68, 23, -60, -89, 126, 61, 100, 93, 25, 115, 96, -127, 79, -36, 34, 42, -112, -120, 70, -18, -72, 20, -34, 94, 11, -37, -32, 50, 58, 10, 73, 6, 36, 92, -62, -45, -84, 98, -111, -107, -28, 121, -25, -56, 55, 109, -115, -43, 78, -87, 108, 86, -12, -22, 101, 122, -82, 8, -70, 120, 37, 46, 28, -90, -76, -58, -24, -35, 116, 31, 75, -67, -117, -118, 112, 62, -75, 102, 72, 3, -10, 14, 97, 53, 87, -71, -122, -63, 29, -98, -31, -8, -104, 17, 105, -39, -114, -108, -101, 30, -121, -23, -50, 85, 40, -33, -116, -95, -119, 13, -65, -26, 66, 104, 65, -103, 45, 15, -80, 84, -69, 22};
    static final byte[] Si = new byte[]{82, 9, 106, -43, 48, 54, -91, 56, -65, 64, -93, -98, -127, -13, -41, -5, 124, -29, 57, -126, -101, 47, -1, -121, 52, -114, 67, 68, -60, -34, -23, -53, 84, 123, -108, 50, -90, -62, 35, 61, -18, 76, -107, 11, 66, -6, -61, 78, 8, 46, -95, 102, 40, -39, 36, -78, 118, 91, -94, 73, 109, -117, -47, 37, 114, -8, -10, 100, -122, 104, -104, 22, -44, -92, 92, -52, 93, 101, -74, -110, 108, 112, 72, 80, -3, -19, -71, -38, 94, 21, 70, 87, -89, -115, -99, -124, -112, -40, -85, 0, -116, -68, -45, 10, -9, -28, 88, 5, -72, -77, 69, 6, -48, 44, 30, -113, -54, 63, 15, 2, -63, -81, -67, 3, 1, 19, -118, 107, 58, -111, 17, 65, 79, 103, -36, -22, -105, -14, -49, -50, -16, -76, -26, 115, -106, -84, 116, 34, -25, -83, 53, -123, -30, -7, 55, -24, 28, 117, -33, 110, 71, -15, 26, 113, 29, 41, -59, -119, 111, -73, 98, 14, -86, 24, -66, 27, -4, 86, 62, 75, -58, -46, 121, 32, -102, -37, -64, -2, 120, -51, 90, -12, 31, -35, -88, 51, -120, 7, -57, 49, -79, 18, 16, 89, 39, -128, -20, 95, 96, 81, 127, -87, 25, -75, 74, 13, 45, -27, 122, -97, -109, -55, -100, -17, -96, -32, 59, 77, -82, 42, -11, -80, -56, -21, -69, 60, -125, 83, -103, 97, 23, 43, 4, 126, -70, 119, -42, 38, -31, 105, 20, 99, 85, 33, 12, 125};
    static final byte[] rcon = new byte[]{0, 1, 2, 4, 8, 16, 32, 64, -128, 27, 54, 108, -40, -85, 77, -102, 47, 94, -68, 99, -58, -105, 53, 106, -44, -77, 125, -6, -17, -59, -111};
    public static final int COL_SIZE = 4;
    public static final int NUM_COLS = 4;
    public static final int ROOT = 283;
    static final int[] row_shift = new int[]{0, 1, 2, 3};
    static final int[] alog = new int[256];
    static final int[] log = new int[256];

    public AES() {
    }

    public static int getRounds(int keySize) {
        switch(keySize) {
        case 16:
            return 10;
        case 24:
            return 12;
        default:
            return 14;
        }
    }

    static final int mul(int a, int b) {
        return a != 0 && b != 0?alog[(log[a & 255] + log[b & 255]) % 255]:0;
    }

    public static void trace_static() {
        System.out.print("AES Static Tablesn");
        System.out.print("S[] = n");

        int i;
        int j;
        for(i = 0; i < 16; ++i) {
            for(j = 0; j < 16; ++j) {
                System.out.print(Util.toHEX1(S[i * 16 + j]) + ", ");
            }

            System.out.println();
        }

        System.out.print("Si[] = n");

        for(i = 0; i < 16; ++i) {
            for(j = 0; j < 16; ++j) {
                System.out.print(Util.toHEX1(Si[i * 16 + j]) + ", ");
            }

            System.out.println();
        }

        System.out.print("rcon[] = n");

        for(i = 0; i < 5; ++i) {
            for(j = 0; j < 6; ++j) {
                System.out.print(Util.toHEX1(rcon[i * 6 + j]) + ", ");
            }

            System.out.println();
        }

        System.out.print("log[] = n");

        for(i = 0; i < 32; ++i) {
            for(j = 0; j < 8; ++j) {
                System.out.print(Util.toHEX1(log[i * 8 + j]) + ", ");
            }

            System.out.println();
        }

        System.out.print("alog[] = n");

        for(i = 0; i < 32; ++i) {
            for(j = 0; j < 8; ++j) {
                System.out.print(Util.toHEX1(alog[i * 8 + j]) + ", ");
            }

            System.out.println();
        }

    }

    public byte[] encrypt(byte[] plain) {
        byte[] a = new byte[16];
        byte[] ta = new byte[16];
        this.traceInfo = "";
        if(this.traceLevel > 0) {
            this.traceInfo = "encryptAES(" + Util.toHEX1(plain) + ")";
        }

        if(plain == null) {
            throw new IllegalArgumentException("Empty plaintext");
        } else if(plain.length != 16) {
            throw new IllegalArgumentException("Incorrect plaintext length");
        } else {
            byte[] Ker = this.Ke[0];

            int i;
            for(i = 0; i < 16; ++i) {
                a[i] = (byte)(plain[i] ^ Ker[i]);
            }

            if(this.traceLevel > 2) {
                this.traceInfo = this.traceInfo + "n  R0 (Key = " + Util.toHEX1(Ker) + ")ntAK = " + Util.toHEX1(a);
            } else if(this.traceLevel > 1) {
                this.traceInfo = this.traceInfo + "n  R0 (Key = " + Util.toHEX1(Ker) + ")t = " + Util.toHEX1(a);
            }

            int k;
            int row;
            for(int r = 1; r < this.numRounds; ++r) {
                Ker = this.Ke[r];
                if(this.traceLevel > 1) {
                    this.traceInfo = this.traceInfo + "n  R" + r + " (Key = " + Util.toHEX1(Ker) + ")t";
                }

                for(i = 0; i < 16; ++i) {
                    ta[i] = S[a[i] & 255];
                }

                if(this.traceLevel > 2) {
                    this.traceInfo = this.traceInfo + "ntSB = " + Util.toHEX1(ta);
                }

                for(i = 0; i < 16; ++i) {
                    row = i % 4;
                    k = (i + row_shift[row] * 4) % 16;
                    a[i] = ta[k];
                }

                if(this.traceLevel > 2) {
                    this.traceInfo = this.traceInfo + "ntSR = " + Util.toHEX1(a);
                }

                for(int col = 0; col < 4; ++col) {
                    i = col * 4;
                    ta[i] = (byte)(mul(2, a[i]) ^ mul(3, a[i + 1]) ^ a[i + 2] ^ a[i + 3]);
                    ta[i + 1] = (byte)(a[i] ^ mul(2, a[i + 1]) ^ mul(3, a[i + 2]) ^ a[i + 3]);
                    ta[i + 2] = (byte)(a[i] ^ a[i + 1] ^ mul(2, a[i + 2]) ^ mul(3, a[i + 3]));
                    ta[i + 3] = (byte)(mul(3, a[i]) ^ a[i + 1] ^ a[i + 2] ^ mul(2, a[i + 3]));
                }

                if(this.traceLevel > 2) {
                    this.traceInfo = this.traceInfo + "ntMC = " + Util.toHEX1(ta);
                }

                for(i = 0; i < 16; ++i) {
                    a[i] = (byte)(ta[i] ^ Ker[i]);
                }

                if(this.traceLevel > 2) {
                    this.traceInfo = this.traceInfo + "ntAK";
                }

                if(this.traceLevel > 1) {
                    this.traceInfo = this.traceInfo + " = " + Util.toHEX1(a);
                }
            }

            Ker = this.Ke[this.numRounds];
            if(this.traceLevel > 1) {
                this.traceInfo = this.traceInfo + "n  R" + this.numRounds + " (Key = " + Util.toHEX1(Ker) + ")t";
            }

            for(i = 0; i < 16; ++i) {
                a[i] = S[a[i] & 255];
            }

            if(this.traceLevel > 2) {
                this.traceInfo = this.traceInfo + "ntSB = " + Util.toHEX1(a);
            }

            for(i = 0; i < 16; ++i) {
                row = i % 4;
                k = (i + row_shift[row] * 4) % 16;
                ta[i] = a[k];
            }

            if(this.traceLevel > 2) {
                this.traceInfo = this.traceInfo + "ntSR = " + Util.toHEX1(a);
            }

            for(i = 0; i < 16; ++i) {
                a[i] = (byte)(ta[i] ^ Ker[i]);
            }

            if(this.traceLevel > 2) {
                this.traceInfo = this.traceInfo + "ntAK";
            }

            if(this.traceLevel > 1) {
                this.traceInfo = this.traceInfo + " = " + Util.toHEX1(a) + "n";
            }

            if(this.traceLevel > 0) {
                this.traceInfo = this.traceInfo + " = " + Util.toHEX1(a) + "n";
            }

            return a;
        }
    }

    public byte[] decrypt(byte[] cipher) {
        byte[] a = new byte[16];
        byte[] ta = new byte[16];
        this.traceInfo = "";
        if(this.traceLevel > 0) {
            this.traceInfo = "decryptAES(" + Util.toHEX1(cipher) + ")";
        }

        if(cipher == null) {
            throw new IllegalArgumentException("Empty ciphertext");
        } else if(cipher.length != 16) {
            throw new IllegalArgumentException("Incorrect ciphertext length");
        } else {
            byte[] Kdr = this.Kd[0];

            int i;
            for(i = 0; i < 16; ++i) {
                a[i] = (byte)(cipher[i] ^ Kdr[i]);
            }

            if(this.traceLevel > 2) {
                this.traceInfo = this.traceInfo + "n  R0 (Key = " + Util.toHEX1(Kdr) + ")nt AK = " + Util.toHEX1(a);
            } else if(this.traceLevel > 1) {
                this.traceInfo = this.traceInfo + "n  R0 (Key = " + Util.toHEX1(Kdr) + ")t = " + Util.toHEX1(a);
            }

            int k;
            int row;
            for(int r = 1; r < this.numRounds; ++r) {
                Kdr = this.Kd[r];
                if(this.traceLevel > 1) {
                    this.traceInfo = this.traceInfo + "n  R" + r + " (Key = " + Util.toHEX1(Kdr) + ")t";
                }

                for(i = 0; i < 16; ++i) {
                    row = i % 4;
                    k = (i + 16 - row_shift[row] * 4) % 16;
                    ta[i] = a[k];
                }

                if(this.traceLevel > 2) {
                    this.traceInfo = this.traceInfo + "ntISR = " + Util.toHEX1(ta);
                }

                for(i = 0; i < 16; ++i) {
                    a[i] = Si[ta[i] & 255];
                }

                if(this.traceLevel > 2) {
                    this.traceInfo = this.traceInfo + "ntISB = " + Util.toHEX1(a);
                }

                for(i = 0; i < 16; ++i) {
                    ta[i] = (byte)(a[i] ^ Kdr[i]);
                }

                if(this.traceLevel > 2) {
                    this.traceInfo = this.traceInfo + "nt AK = " + Util.toHEX1(ta);
                }

                for(int col = 0; col < 4; ++col) {
                    i = col * 4;
                    a[i] = (byte)(mul(14, ta[i]) ^ mul(11, ta[i + 1]) ^ mul(13, ta[i + 2]) ^ mul(9, ta[i + 3]));
                    a[i + 1] = (byte)(mul(9, ta[i]) ^ mul(14, ta[i + 1]) ^ mul(11, ta[i + 2]) ^ mul(13, ta[i + 3]));
                    a[i + 2] = (byte)(mul(13, ta[i]) ^ mul(9, ta[i + 1]) ^ mul(14, ta[i + 2]) ^ mul(11, ta[i + 3]));
                    a[i + 3] = (byte)(mul(11, ta[i]) ^ mul(13, ta[i + 1]) ^ mul(9, ta[i + 2]) ^ mul(14, ta[i + 3]));
                }

                if(this.traceLevel > 2) {
                    this.traceInfo = this.traceInfo + "ntIMC";
                }

                if(this.traceLevel > 1) {
                    this.traceInfo = this.traceInfo + " = " + Util.toHEX1(a);
                }
            }

            Kdr = this.Kd[this.numRounds];
            if(this.traceLevel > 1) {
                this.traceInfo = this.traceInfo + "n  R" + this.numRounds + " (Key = " + Util.toHEX1(Kdr) + ")t";
            }

            for(i = 0; i < 16; ++i) {
                row = i % 4;
                k = (i + 16 - row_shift[row] * 4) % 16;
                ta[i] = a[k];
            }

            if(this.traceLevel > 2) {
                this.traceInfo = this.traceInfo + "ntISR = " + Util.toHEX1(a);
            }

            for(i = 0; i < 16; ++i) {
                ta[i] = Si[ta[i] & 255];
            }

            if(this.traceLevel > 2) {
                this.traceInfo = this.traceInfo + "ntISB = " + Util.toHEX1(a);
            }

            for(i = 0; i < 16; ++i) {
                a[i] = (byte)(ta[i] ^ Kdr[i]);
            }

            if(this.traceLevel > 2) {
                this.traceInfo = this.traceInfo + "nt AK";
            }

            if(this.traceLevel > 1) {
                this.traceInfo = this.traceInfo + " = " + Util.toHEX1(a) + "n";
            }

            if(this.traceLevel > 0) {
                this.traceInfo = this.traceInfo + " = " + Util.toHEX1(a) + "n";
            }

            return a;
        }
    }

    public void setKey(byte[] key) {
        boolean BC = true;
        int Klen = key.length;
        int Nk = Klen / 4;
        this.traceInfo = "";
        if(this.traceLevel > 0) {
            this.traceInfo = "setKey(" + Util.toHEX1(key) + ")n";
        }

        if(key == null) {
            throw new IllegalArgumentException("Empty key");
        } else if(key.length != 16 && key.length != 24 && key.length != 32) {
            throw new IllegalArgumentException("Incorrect key length");
        } else {
            this.numRounds = getRounds(Klen);
            int ROUND_KEY_COUNT = (this.numRounds + 1) * 4;
            byte[] w0 = new byte[ROUND_KEY_COUNT];
            byte[] w1 = new byte[ROUND_KEY_COUNT];
            byte[] w2 = new byte[ROUND_KEY_COUNT];
            byte[] w3 = new byte[ROUND_KEY_COUNT];
            this.Ke = new byte[this.numRounds + 1][16];
            this.Kd = new byte[this.numRounds + 1][16];
            int i = 0;

            int j;
            for(j = 0; i < Nk; ++i) {
                w0[i] = key[j++];
                w1[i] = key[j++];
                w2[i] = key[j++];
                w3[i] = key[j++];
            }

            for(i = Nk; i < ROUND_KEY_COUNT; ++i) {
                byte t0 = w0[i - 1];
                byte t1 = w1[i - 1];
                byte t2 = w2[i - 1];
                byte t3 = w3[i - 1];
                if(i % Nk == 0) {
                    byte old0 = t0;
                    t0 = (byte)(S[t1 & 255] ^ rcon[i / Nk]);
                    t1 = S[t2 & 255];
                    t2 = S[t3 & 255];
                    t3 = S[old0 & 255];
                } else if(Nk > 6 && i % Nk == 4) {
                    t0 = S[t0 & 255];
                    t1 = S[t1 & 255];
                    t2 = S[t2 & 255];
                    t3 = S[t3 & 255];
                }

                w0[i] = (byte)(w0[i - Nk] ^ t0);
                w1[i] = (byte)(w1[i - Nk] ^ t1);
                w2[i] = (byte)(w2[i - Nk] ^ t2);
                w3[i] = (byte)(w3[i - Nk] ^ t3);
            }

            int r = 0;

            for(i = 0; r < this.numRounds + 1; ++r) {
                for(j = 0; j < 4; ++j) {
                    this.Ke[r][4 * j] = w0[i];
                    this.Ke[r][4 * j + 1] = w1[i];
                    this.Ke[r][4 * j + 2] = w2[i];
                    this.Ke[r][4 * j + 3] = w3[i];
                    this.Kd[this.numRounds - r][4 * j] = w0[i];
                    this.Kd[this.numRounds - r][4 * j + 1] = w1[i];
                    this.Kd[this.numRounds - r][4 * j + 2] = w2[i];
                    this.Kd[this.numRounds - r][4 * j + 3] = w3[i];
                    ++i;
                }
            }

            if(this.traceLevel > 3) {
                this.traceInfo = this.traceInfo + "  Encrypt Round keys:n";

                for(r = 0; r < this.numRounds + 1; ++r) {
                    this.traceInfo = this.traceInfo + "  R" + r + "t = " + Util.toHEX1(this.Ke[r]) + "n";
                }

                this.traceInfo = this.traceInfo + "  Decrypt Round keys:n";

                for(r = 0; r < this.numRounds + 1; ++r) {
                    this.traceInfo = this.traceInfo + "  R" + r + "t = " + Util.toHEX1(this.Kd[r]) + "n";
                }
            }

        }
    }

    public static void self_test(String hkey, String hplain, String hcipher, int lev) {
        byte[] key = Util.hex2byte(hkey);
        byte[] plain = Util.hex2byte(hplain);
        byte[] cipher = Util.hex2byte(hcipher);
        AES testAES = new AES();
        testAES.traceLevel = lev;
        testAES.setKey(key);
        System.out.print(testAES.traceInfo);
        byte[] result = testAES.encrypt(plain);
        System.out.print(testAES.traceInfo);
        if(Arrays.equals(result, cipher)) {
            System.out.print("Test OKn");
        } else {
            System.out.print("Test Failed. Result was " + Util.toHEX(result) + "n");
        }

        result = testAES.decrypt(cipher);
        System.out.print(testAES.traceInfo);
        if(Arrays.equals(result, plain)) {
            System.out.print("Test OKn");
        } else {
            System.out.print("Test Failed. Result was " + Util.toHEX(result) + "n");
        }

        System.out.println();
    }

    public static String static_byteArrayToString(byte[] data) {
        String res = "";
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < data.length; ++i) {
            int n = data[i];
            if(n < 0) {
                n += 256;
            }

            sb.append((char)n);
        }

        res = sb.toString();
        return res;
    }

    public static byte[] static_stringToByteArray(String s) {
        byte[] temp = new byte[s.length()];

        for(int i = 0; i < s.length(); ++i) {
            temp[i] = (byte)s.charAt(i);
        }

        return temp;
    }

    public static String static_intArrayToString(int[] t) {
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < t.length; ++i) {
            sb.append((char)t[i]);
        }

        return sb.toString();
    }

    public String _cryptAll(String data, int mode) {
        AES aes = this;
        data = append(data);
        int nParts = data.length() / 16;
        byte[] res = new byte[data.length()];
        String partStr = "";
        byte[] partByte = new byte[16];

        for(int p = 0; p < nParts; ++p) {
            partStr = data.substring(p * 16, p * 16 + 16);
            partByte = static_stringToByteArray(partStr);
            if(mode == 1) {
                partByte = aes.encrypt(partByte);
            }

            if(mode == 2) {
                partByte = aes.decrypt(partByte);
            }

            for(int b = 0; b < 16; ++b) {
                res[p * 16 + b] = partByte[b];
            }
        }

        return static_byteArrayToString(res);
    }

    public byte[] _cryptAll(byte[] data, int mode) {
        AES aes = this;
        data = append(data);
        int nParts = data.length / 16;
        byte[] res = new byte[data.length];
        byte[] partByte = new byte[16];

        for(int p = 0; p < nParts; ++p) {
            System.arraycopy(data, p * 16, partByte, 0, 16);
            if(mode == 1) {
                partByte = aes.encrypt(partByte);
            }

            if(mode == 2) {
                partByte = aes.decrypt(partByte);
            }

            for(int b = 0; b < 16; ++b) {
                res[p * 16 + b] = partByte[b];
            }
        }

        return res;
    }

    public static byte[] append(byte[] data) {
        int length = data.length;
        if(length % 16 <= 0) {
            return data;
        } else {
            int rest = 16 - length % 16;
            byte[] rc = new byte[length + rest];
            System.arraycopy(data, 0, rc, 0, data.length);

            for(int i = 0; i < rest; ++i) {
                data[length + i] = (byte)(data[length + i] + 32);
            }

            return rc;
        }
    }

    public static String append(String data) {
        if(data.length() % 16 > 0) {
            int rest = 16 - data.length() % 16;

            for(int i = 0; i < rest; ++i) {
                data = data + " ";
            }
        }

        return data;
    }

    public String Encrypt(String data) {
        return this._cryptAll((String)data, 1);
    }

    public String Decrypt(String data) {
        return this._cryptAll((String)data, 2);
    }

    public void setKey(String key) {
        this.setKey(static_stringToByteArray(key));
    }

    public static void main(String[] args) {
        String cryptKey = "aPb4x9q0H4W8rPs7aPb4x9q0H4W8r@s7";
        String data = "hello world";
        data = append(data);
        AES aes = new AES();
        aes.setKey(cryptKey);
        byte[] t1 = aes.encrypt(data.getBytes());
        System.out.println("data = " + Util.toHEX1(t1));
        byte[] t2 = aes.decrypt(t1);
        System.out.println("data = " + new String(t2));
    }

    static {
        alog[0] = 1;

        int i;
        for(i = 1; i < 256; ++i) {
            int j = alog[i - 1] << 1 ^ alog[i - 1];
            if((j & 256) != 0) {
                j ^= 283;
            }

            alog[i] = j;
        }

        for(i = 1; i < 255; log[alog[i]] = i++) {
            ;
        }

    }
}
