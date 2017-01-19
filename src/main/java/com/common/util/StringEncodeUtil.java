//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;

public class StringEncodeUtil {
    static List<String> src = new ArrayList();
    static List<String> out = new ArrayList();
    static List<String> letters = new ArrayList();
    static String initialize = "685739";

    public StringEncodeUtil() {
    }

    public static void main(String[] args) {
        Long in = Long.valueOf(1L);
        String en = encrypt(in);
        System.out.println("en:" + en);
        String de = "";

        try {
            de = decrypt(en);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        System.out.println("de:" + de);
    }

    public static String encrypt(Long value) {
        if(value == null) {
            return null;
        } else {
            String str = value.toString();
            String letter = getLetter(str);
            str = initialize + str;
            StringBuffer result = new StringBuffer();

            for(int i = 0; i < str.length(); ++i) {
                String subChar = str.substring(i, i + 1);
                if(!StringUtils.isEmpty(subChar)) {
                    int index = src.indexOf(subChar);
                    if(index >= 0) {
                        result.append((String)out.get(index));
                    }
                }
            }

            return letter + result.toString();
        }
    }

    public static String decrypt(String str) throws Exception {
        if(StringUtils.isEmpty(str)) {
            return null;
        } else {
            String letter = str.replaceAll("\\d+", "");
            String number = str.replace(letter, "");
            StringBuffer letNumber = new StringBuffer();
            StringBuffer result = new StringBuffer();

            int letNumberStr;
            String resultStr;
            int letterLeng;
            for(letNumberStr = 0; letNumberStr < letter.length(); ++letNumberStr) {
                resultStr = letter.substring(letNumberStr, letNumberStr + 1);
                if(!StringUtils.isEmpty(resultStr)) {
                    letterLeng = letters.indexOf(resultStr);
                    if(letterLeng >= 0) {
                        letNumber.append((String)src.get(letterLeng));
                    }
                }
            }

            for(letNumberStr = 0; letNumberStr < number.length(); ++letNumberStr) {
                resultStr = number.substring(letNumberStr, letNumberStr + 1);
                if(!StringUtils.isEmpty(resultStr)) {
                    letterLeng = out.indexOf(resultStr);
                    if(letterLeng >= 0) {
                        result.append((String)src.get(letterLeng));
                    }
                }
            }

            String var10 = letNumber.toString();
            resultStr = result.toString();
            if(!StringUtils.isEmpty(var10) && !StringUtils.isEmpty(resultStr)) {
                letterLeng = Integer.parseInt(var10.substring(0, 1));
                if(resultStr.length() < var10.length()) {
                    throw new Exception("the decoding error [result:" + resultStr + "]");
                } else {
                    String lett = resultStr.substring(0, var10.length());
                    if(lett.equalsIgnoreCase(var10)) {
                        resultStr = resultStr.substring(letter.length(), resultStr.length());
                        if(var10.length() < letterLeng) {
                            throw new Exception("the decoding error [letNumberStr:" + var10 + "]");
                        } else {
                            String id = var10.substring(letterLeng, var10.length());
                            if(id.equals(resultStr)) {
                                return resultStr;
                            } else {
                                throw new Exception("the value after decoding isn\'t same [id:" + id + "] [result:" + resultStr + "]");
                            }
                        }
                    } else {
                        throw new Exception("the decoding fails [result:" + resultStr + "]");
                    }
                }
            } else {
                throw new Exception("the decoding null [result:" + resultStr + "]");
            }
        }
    }

    public static String getLetter(String str) {
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString();
        StringBuffer sb = new StringBuffer();

        for(int num = 0; num < uuidStr.length(); ++num) {
            String letter = uuidStr.substring(num, num + 1);
            if(isNumeric(letter)) {
                sb.append(letter);
            }
        }

        String var9 = sb.toString();
        if(var9 != null && var9.length() >= 8) {
            initialize = "9" + var9.substring(0, 8) + str;
        } else {
            initialize = var9.length() + 1 + var9 + str;
        }

        StringBuffer var10 = new StringBuffer();

        for(int i = 0; i < initialize.length(); ++i) {
            String subChar = initialize.substring(i, i + 1);
            if(!StringUtils.isEmpty(subChar)) {
                int index = src.indexOf(subChar);
                if(index >= 0) {
                    var10.append((String)letters.get(index));
                }
            }
        }

        return var10.toString();
    }

    public static boolean isNumeric(String str) {
        int i = str.length();

        char chr;
        do {
            --i;
            if(i < 0) {
                return true;
            }

            chr = str.charAt(i);
        } while(chr >= 48 && chr <= 57);

        return false;
    }

    static {
        for(int i = 0; i < 10; ++i) {
            src.add(i + "");
        }

        out.add("6");
        out.add("5");
        out.add("0");
        out.add("4");
        out.add("7");
        out.add("3");
        out.add("8");
        out.add("9");
        out.add("2");
        out.add("1");
        letters.add("b");
        letters.add("x");
        letters.add("c");
        letters.add("d");
        letters.add("w");
        letters.add("g");
        letters.add("h");
        letters.add("e");
        letters.add("j");
        letters.add("f");
    }
}
