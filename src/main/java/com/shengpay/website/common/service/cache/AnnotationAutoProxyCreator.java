/**
 * Shengpay.com Inc.
 * Copyright (c) 2004-2011 All Rights Reserved.
 */
package com.shengpay.website.common.service.cache;

import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * 为带@Cache 标签的类创建代理。
 * @author sunzhi.tom
 * @version $Id: AutoCreator.java, v 0.1 2011-8-15 上午10:17:18 sunzhi.tom Exp $
 */
public class AnnotationAutoProxyCreator extends AbstractAutoProxyCreator {

    /**  */
    private static final long serialVersionUID = 7635973429179069425L;

    /** 
     * @see org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator#getAdvicesAndAdvisorsForBean(java.lang.Class, java.lang.String, org.springframework.aop.TargetSource)
     */
    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName,
                                                    TargetSource customTargetSource)
                                                                                    throws BeansException {
        if (AnnotationUtils.findAnnotation(beanClass, Cache.class) != null) {
            return PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS;
        } else {
            return DO_NOT_PROXY;
        }
    }
}
