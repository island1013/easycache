/**
 * Shengpay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.shengpay.website.common.service.util;

import com.sdo.common.lang.StringUtil;

/**
 * 
 * @author sunzhi.tom
 * @version $Id: SndaCardUtil.java, v 0.1 2012-1-9 下午7:30:49 sunzhi.tom Exp $
 */
public class SndaCardUtil {
    public static boolean isMarkCardNo(String cardNo) {
        String mark = "";
        if (cardNo.length() == 15) {
            mark = "*****";
        } else if (cardNo.length() == 16) {
            mark = "******";
        }
        return StringUtil.indexOf(cardNo, mark) == 6 && StringUtil.indexOf(cardNo, '*') == 6
               && StringUtil.lastIndexOf(cardNo, '*') == 5 + mark.length();
    }

    public static boolean isCardEquals(String card1, String card2) {
        if (StringUtil.isBlank(card1) || StringUtil.isBlank(card2)) {
            return false;
        }
        return StringUtil.equals(StringUtil.substring(card1, 0, 6),
            StringUtil.substring(card2, 0, 6))
               && StringUtil.equals(
                   StringUtil.substring(card1, card1.length() - 4, card1.length()),
                   StringUtil.substring(card2, card2.length() - 4, card2.length()));
    }
}
