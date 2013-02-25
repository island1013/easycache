/**
 * Shengpay.com Inc.
 * Copyright (c) 2004-2011 All Rights Reserved.
 */
package com.shengpay.website.common.service.util;

/**
 * 
 * @author sunzhi.tom
 * @version $Id: HexUtil.java, v 0.1 2011-8-8 下午3:20:18 sunzhi.tom Exp $
 */
public class HexUtil {

    private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
            'b', 'c', 'd', 'e', 'f'   };

    public static String encodeHex(byte[] data) {

        int l = data.length;

        char[] out = new char[l << 2];

        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = '0';
            out[j++] = 'x';
            out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS[0x0F & data[i]];
        }

        return String.valueOf(out);
    }
}
