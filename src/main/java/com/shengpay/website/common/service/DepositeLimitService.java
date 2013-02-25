/*
 * Copyright 2010 sdp.com, Inc. All rights reserved.
 * sdp.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * creator : liuxiang.bruce
 * create time : 2011-10-20 下午03:54:30
 */
package com.shengpay.website.common.service;

import java.math.BigDecimal;

/**
 * 功能描述：
 * @author liuxiang.bruce
 * time : 2011-10-20 下午03:54:30
 */
 public interface DepositeLimitService {
     /**
      * 查询是否超过限制
      * 包括次数、总金额
      * @param ptid
      * @param ruleID
      * @return
      */
     public DepositLimitResponse queryLimit(String ptid, String ruleID);

     /**
      * 查询是否超过限制
      * 包括次数、总金额
      * 判断规则，当前的充值金额+已经充值的金额 是否大于规则定的金额
      * @param ptid
      * @param ruleID
      * @param currentDepositAmount 当前的充值金额
      * @return
      */
     public DepositLimitResponse queryLimit(String ptid, String ruleID, BigDecimal currentDepositAmount);

}
