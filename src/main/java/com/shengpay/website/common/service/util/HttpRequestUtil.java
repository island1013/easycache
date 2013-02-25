/**
 * Shengpay.com Inc.
 * Copyright (c) 2004-2011 All Rights Reserved.
 */
package com.shengpay.website.common.service.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sdo.common.lang.StringUtil;

/**
 * 
 * @author sunzhi.tom
 * @version $Id: HttpRequestUtil.java, v 0.1 2011-8-31 下午2:48:40 sunzhi.tom Exp $
 */
public class HttpRequestUtil {
    public static String getRequestString(HttpServletRequest request) {
        try {
            String requestURI = StringUtil.substring(request.getRequestURI(), 1);
            String queryString = request.getQueryString();
            if (StringUtil.isNotBlank(queryString)) {
                requestURI += "?" + queryString;
            }
            return URLEncoder.encode(requestURI, "gb2312");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String getAllRequestString(HttpServletRequest request) {
        String requestURI = "";
        String queryString = request.getQueryString();
        if (StringUtil.isNotBlank(queryString)) {
            requestURI += queryString;
        }
        return request.getRequestURL().toString() + "?" + requestURI;
    }

    public static String getRequestStringExclusive(HttpServletRequest request, String param) {
        try {
            String requestURI = StringUtil.substring(request.getRequestURI(), 1);
            StringBuilder sb = new StringBuilder(requestURI);
            Map parameterMap = request.getParameterMap();
            if (parameterMap != null && parameterMap.size() > 0) {
                sb.append("?");
                for (Object object : parameterMap.keySet()) {
                    String key = (String) object;
                    if (StringUtil.equalsIgnoreCase(param, key)) {
                        continue;
                    }
                    String value = request.getParameter(key);
                    sb.append(URLEncoder.encode(key, "gb2312")).append("=")
                        .append(URLEncoder.encode(value, "gb2312")).append("&");
                }
                requestURI = sb.substring(0, sb.length() - 1);
            }
            return requestURI;
        } catch (UnsupportedEncodingException e) {
            return null;
        }

    }
}
