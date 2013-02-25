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
 * @version $Id: NetUtil.java, v 0.1 2011-10-3 下午6:13:22 sunzhi.tom Exp $
 */
public class NetUtil {
    /**
     * 是否是旧的.net
     * 
     * @param request
     * @return
     */
    public static boolean isNewNetSite(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (StringUtil.isNotBlank(referer) && referer.indexOf("index.aspx?type") > -1) {
            return false;
        } else {
            return true;
        }
    }

}
