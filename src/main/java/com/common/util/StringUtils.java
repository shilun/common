//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

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
        Pattern p = Pattern.compile("^1[34578]\\d{9}$");
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

    public static String getDomainName(String url) {
        url = url.toLowerCase();
        Matcher m = Pattern.compile("^http://[^/]+").matcher(url);
        return m.find()?m.group():"";
    }

    public static String getSubTitleName(String title, Integer subSize) {
        return isNotBlank(title)?(title.length() > subSize.intValue()?title.substring(0, subSize.intValue()) + "....":title):"";
    }

    public static boolean isContains(String text, String subText) {
        return text.contains(subText);
    }

    public static void main(String[] args) {
        System.out.println(isMobileNO("13541370905"));
        System.out.println(isMobileNO("14541370905"));
        System.out.println(isMobileNO("15741370905"));
        System.out.println(isMobileNO("17413709085"));
        System.out.println(isMobileNO("18413709085"));
    }
}
