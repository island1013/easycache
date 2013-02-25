/**
 * Shengpay.com Inc.
 * Copyright (c) 2004-2011 All Rights Reserved.
 */
package com.shengpay.website.common.service.util;

import com.sdo.common.lang.StringUtil;

/**
 * 
 * @author sunzhi.tom
 * @version $Id: BankCardUtil.java, v 0.1 2011-9-6 下午3:40:02 sunzhi.tom Exp $
 */
public class BankCardUtil {
    public static String parseBankCard(String bankCard) {
        if (bankCard == null || bankCard.length() <= 4) {
            return bankCard;
        }
        return StringUtil.alignRight(bankCard.substring(bankCard.length() - 4, bankCard.length()),
            bankCard.length(), '*');
    }
}
