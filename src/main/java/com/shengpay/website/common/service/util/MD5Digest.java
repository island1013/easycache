/**
 * 
 */
package com.shengpay.website.common.service.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sdo.common.util.DateUtil;

public final class MD5Digest {

    private static Logger logger = LoggerFactory.getLogger(MD5Digest.class);

    /**
    * MD5加密字符串，返回加密后的16进制字符串
    * @param text
    * @return
     * @throws UnsupportedEncodingException 
    */
    public static String getDigestString(String text) {
        return toHexString(getDigest(text, "UTF-8"));
    }

    public static String GetDigestString(String text, String encoding) {
        return toHexString(getDigest(text, encoding));
    }

    /**
    * MD5加密字符串，返回加密后的字节数组
    * @param text
    * @return
     * @throws UnsupportedEncodingException 
    */
    public static byte[] getDigest(String text, String encoding) {
        try {
            return getDigest(text.getBytes(encoding));
        } catch (UnsupportedEncodingException e) {
            logger.error("不支持 " + encoding + " 编码", e);
            return null;
        }
    }

    /**
    * MD5加密字节数组，返回加密后的字节数组
    * @param bytes
    * @return
    */
    public static byte[] getDigest(byte[] bytes) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            return md.digest(bytes);
        } catch (NoSuchAlgorithmException e) {
            logger.error("不支持 MD5算法", e);
            return new byte[0];
        }

    }

    public static String toHexString(byte[] b) {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < b.length; ++i) {
            buffer.append(toHexString(b[i]));
        }
        return buffer.toString();
    }

    public static void main(String[] args) {
        String ss = "2011-8-17 19:13:13||127.0.0.1||0||MP00000056302LLW.sdo||||||13636518375||MP00000056302LLW.sdo||3.00||1||0||ICBC||||04||127.0.0.1||sh-sunzhi||0051||||EC51F92A-F675-42FF-8A0A-C9831A2D0889";
        System.out.println(getDigestString(ss));
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-M-d HH:mm:ss");
        System.out.println(dateFormat.format(date));
    }

    private static String toHexString(byte b) {
        char[] buffer = new char[2];
        buffer[0] = Character.forDigit((b >>> 4) & 0x0F, 16);
        buffer[1] = Character.forDigit(b & 0x0F, 16);
        return new String(buffer);
    }

}
