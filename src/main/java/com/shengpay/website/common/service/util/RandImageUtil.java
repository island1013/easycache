/**
 * Shengpay.com Inc.
 * Copyright (c) 2004-2011 All Rights Reserved.
 */
package com.shengpay.website.common.service.util;

import javax.servlet.http.HttpServletRequest;

import com.sdo.common.lang.StringUtil;

/**
 * 
 * @author sunzhi.tom
 * @version $Id: RandImageUtil.java, v 0.1 2011-8-18 下午8:01:31 sunzhi.tom Exp $
 */
public class RandImageUtil {
    /**
     * 校验校验码是否有效。
     * 
     * @param randString
     * @param request
     * @return
     */
    public static boolean isValid(String randString, HttpServletRequest request) {
        if (StringUtil.isBlank(randString) || request == null) {
            return false;
        }
        String rand = (String) request.getSession().getAttribute("rand");
        return StringUtil.equalsIgnoreCase(randString, rand);
    }
}
