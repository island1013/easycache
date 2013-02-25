/*
 * Copyright 2010 sdp.com, Inc. All rights reserved.
 * sdp.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * creator : liuxiang.bruce
 * create time : 2011-8-18 下午02:49:04
 */
package com.shengpay.website.common.service.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.shengpay.commom.config.GlobalConfig;

/**
 * 功能描述：
 * @author liuxiang.bruce
 * time : 2011-8-18 下午02:49:04
 */
public class HttpUtil {
    public static boolean isCurrentSiteSfpay(HttpServletRequest request){
        boolean result = false;
        if(null != request){
            String host = request.getHeader("Host");
            if(null != host){
                String hostList = GlobalConfig.getString("sfpayDomain");
                if(hostList.indexOf(host) > -1){
                    result = true;
                    return result;
                }
            }

            //判断cookie
            Cookie[] cookies = request.getCookies();
            Cookie issfpayCookie = null;
            Cookie currrentParentCookie = null;
            for(Cookie cookie:cookies){
                if("Issfpay".equals(cookie.getName())){
                    issfpayCookie = cookie;
                }else if("CurrrentParent".equals(cookie.getName())){
                    currrentParentCookie = cookie;
                }
            }
            if(issfpayCookie != null && "1".equals(issfpayCookie.getValue()) && null != currrentParentCookie && "unknow".equals(currrentParentCookie.getValue())){
                result = true;
                return result;
            }

        }
        return result;
    }
}
