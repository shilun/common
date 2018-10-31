package com.common;

import org.apache.commons.codec.digest.DigestUtils;

public class Test {
    public static void main(String[] args) {
        String context="appid=1&token=6b8f5f76bac247c794a31c7fc2da0d48&encodingKey=57e61c14f4f04aae9a36f04465d7dee3&outTradeNo=100";
        String s = DigestUtils.md5Hex(context);
        System.out.println(s);
    }
}
