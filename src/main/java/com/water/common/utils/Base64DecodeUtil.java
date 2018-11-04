package com.water.common.utils;

import org.apache.commons.codec.binary.Base64;

public class Base64DecodeUtil {

    /**
     * 使用Base64加密算法加密字符串
     * 创建日期2011-4-25上午10:12:38
     * 修改日期
     *return
     */
    public static String base64Encode(String plainText){
        byte[] b=plainText.getBytes();
        Base64 base64=new Base64();
        b=base64.encode(b);
        String s=new String(b);
        return s;
    }

    /**
     * 使用Base64加密
     * 创建日期2011-4-25上午10:15:11
     * 修改日期
     *return
     */
    public static String base64Decode(String encodeStr){
        byte[] b=encodeStr.getBytes();
        Base64 base64=new Base64();
        b=base64.decode(b);
        String s=new String(b);
        return s;
    }
}
