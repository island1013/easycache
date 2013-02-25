/**
 * Shengpay.com Inc.
 * Copyright (c) 2004-2011 All Rights Reserved.
 */
package com.shengpay.website.common.service.cache;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shengpay.website.common.service.util.PropertiesUtil;

/**
 * 方法级缓存的拦截器。
 * @author sunzhi.tom
 * @version $Id: CacheMethodInterceptor.java, v 0.1 2011-8-14 ����11:00:37 Administrator Exp $
 */
public class CacheMethodInterceptor implements MethodInterceptor {
    private static final Logger      logger   = LoggerFactory
                                                  .getLogger(CacheMethodInterceptor.class);

    Map<String, Map<String, Object>> cacheMap = new ConcurrentHashMap<String, Map<String, Object>>();

    /** 
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        CacheMethod cacheMethod = methodInvocation.getMethod().getAnnotation(CacheMethod.class);
        if (cacheMethod == null) {
            return methodInvocation.proceed();
        }
        String methodString = methodInvocation.getMethod().toString();
        String key = buildKey(methodInvocation);
        Object returnObject = null;
        if (cacheMap.containsKey(methodString)) {
            Map<String, Object> entryMap = cacheMap.get(methodString);
            if (!entryMap.containsKey(key)) {
                returnObject = methodInvocation.proceed();
                entryMap.put(key, returnObject);
                if (logger.isInfoEnabled()) {
                    logger.info("缓存数据 key = " + key);
                }
            } else {
                returnObject = entryMap.get(key);
            }
        } else {
            returnObject = methodInvocation.proceed();
            Map<String, Object> entryMap = new ConcurrentHashMap<String, Object>();
            entryMap.put(key, returnObject);
            if (logger.isInfoEnabled()) {
                logger.info("缓存数据 key = " + key);
            }
            cacheMap.put(methodString, entryMap);
        }
        return returnObject;
    }

    /**
     * 刷新缓存。
     */
    public void refresh() {
        for (Map<String, Object> entryMap : cacheMap.values()) {
            entryMap.clear();
        }
        if (logger.isInfoEnabled()) {
            logger.info("刷新数据完毕");
        }
    }

    /**
     * 组装方法缓存的Key。
     * @param methodInvocation
     * @return
     */
    private String buildKey(MethodInvocation methodInvocation) {
        Properties properties = new Properties();
        for (Integer count = 0; count < methodInvocation.getArguments().length; count++) {
            Object argment = methodInvocation.getArguments()[count];
            properties.setProperty(count.toString(), argment == null ? null : argment.toString());
        }
        return PropertiesUtil.convert2String(properties, true);
    }

}