/**
 * Shengpay.com Inc.
 * Copyright (c) 2004-2011 All Rights Reserved.
 */
package com.shengpay.website.common.service.util;


/**
 * 
 * @author sunzhi.tom
 * @version $Id: RSAUtil.java, v 0.1 2011-8-15 下午4:43:27 sunzhi.tom Exp $
 */
public class RSAUtil {
    public static final String  KEY_ALGORITHM       = "RSA";
    public static final String  SIGNATURE_ALGORITHM = "MD5withRSA";

    private static final String PUBLIC_KEY          = "RSAPublicKey";
    private static final String PRIVATE_KEY         = "RSAPrivateKey";

    /** */
    /**    
    * 用私钥对信息生成数字签名    
    *     
    * @param data    
    *            加密数据    
    * @param privateKey    
    *            私钥    
    *     
    * @return    
    * @throws Exception    
    */
    public static String sign(byte[] data, byte[] keyBytes) throws Exception {
        return null;
    }

    public static final byte[] hexStrToBytes(String s) {
        byte[] bytes;

        bytes = new byte[s.length() / 2];

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
        }

        return bytes;
    }
}
