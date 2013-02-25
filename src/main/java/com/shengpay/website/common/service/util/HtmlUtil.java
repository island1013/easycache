/**
 * Shengpay.com Inc.
 * Copyright (c) 2004-2011 All Rights Reserved.
 */
package com.shengpay.website.common.service.util;

import org.springframework.web.util.HtmlUtils;

/**
 * 
 * @author sunzhi.tom
 * @version $Id: UrlUtil.java, v 0.1 2011-9-3 下午5:52:25 sunzhi.tom Exp $
 */
public class HtmlUtil {

    public String decode(String html) {
        return HtmlUtils.htmlUnescape(html);
    }
}
