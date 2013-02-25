/**
 * Shengpay.com Inc.
 * Copyright (c) 2004-2011 All Rights Reserved.
 */
package com.shengpay.website.common.service.cache;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/**
 * 方法级缓存Annotation
 * 切记，该方法的逻辑里面如果出错一定要抛出异常，不能返回null.
 * 切记！！
 * 切记！！！
 * 
 * @author sunzhi.tom
 * @version $Id: CacheMethod.java, v 0.1 2011-9-1 下午12:16:35 sunzhi.tom Exp $
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheMethod {

    boolean isCacheOnStartUp() default true;

    boolean isRefresh() default false;

    int timeout() default 3600;

}
