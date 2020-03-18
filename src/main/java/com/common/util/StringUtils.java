package com.common.util;

import com.common.exception.ApplicationException;

import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils extends org.apache.commons.lang.StringUtils {
    public StringUtils() {
    }

    public boolean isNull(Object o) {
        return o == null;
    }

    public boolean isBlank(Object o) {
        return o == null;
    }

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^1[123456789]\\d{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static boolean isEmail(String email) {
        Pattern pattern = Pattern.compile("^\\w+([-.]\\w+)*@\\w+([-]\\w+)*\\.(\\w+([-]\\w+)*\\.)*[a-z]{2,3}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String getUUID() {
        return replace(UUID.randomUUID().toString(), "-", "");
    }


    public static String getSubTitleName(String title, Integer subSize) {
        return isNotBlank(title) ? (title.length() > subSize.intValue() ? title.substring(0, subSize.intValue()) + "...." : title) : "";
    }

    /**
     * 解析域名
     *
     * @param url
     * @return
     */
    public static String getDomain(String url) {
        int i = url.indexOf("//");
        if (i == -1) {
            throw new ApplicationException("url.format.error");
        }
        url = url.substring(i + 2);
        i = url.indexOf("/");
        if (i == -1) {
            i = url.indexOf("?");
            url = url.substring(0, i);
        } else {
            url = url.substring(0, i);
        }
        i = url.lastIndexOf(".");
        if (i == -1) {
            throw new ApplicationException("url.format.error");
        }
        String temp = url.substring(0, i);
        i = temp.lastIndexOf(".", i);
        url = url.substring(i + 1);
        i = url.indexOf(":");
        if (i != -1) {
            url = url.substring(0, i);
        }
        return url;
    }

    public static final String PW_PATTERN = "[a-z0-9]{1,32}";

    /**
     * 生成mongoid 自动补齐后面的字符
     *
     * @param id
     * @return
     */
    public static String buildMongoId(String id) {
        if (StringUtils.isBlank(id)) {
            throw new ApplicationException("字符内容不能为空");
        }
        if (!id.matches(PW_PATTERN)) {
            throw new ApplicationException("id内容不符合规范");
        }
        return id;
    }

    public static void checkId(String id) {
        if (!id.matches(PW_PATTERN)) {
            throw new ApplicationException("id内容不符合规范");
        }
    }

    public static boolean isContains(String text, String subText) {
        return text.contains(subText);
    }


    /**
     * 生成6位随机验证码
     *
     * @return
     */
    public static String randomSixCode() {
        return String.valueOf(new Random().nextInt(899999) + 100000);
    }

    public static void main(String[] args) {
        System.out.println(getUUID());
    }
}
