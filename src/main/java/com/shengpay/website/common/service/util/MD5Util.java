package com.shengpay.website.common.service.util;

import java.security.MessageDigest;

public class MD5Util {
    public static String encryptMD5(String strInput) {
        StringBuffer buf = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(strInput.getBytes("UTF-8"));
            byte b[] = md.digest();
            buf = new StringBuffer(b.length * 2);
            for (int i = 0; i < b.length; i++) {
                if ((b[i] & 0xff) < 0x10) { /* & 0xff转换无符号整型 */
                    buf.append("0");
                }

                buf.append(Long.toHexString(b[i] & 0xff)); /* 转换16进制,下方法同 */
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return buf.toString();
    }
}
