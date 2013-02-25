/*
 * Copyright 2010 sdp.com, Inc. All rights reserved.
 * sdp.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * creator : liuxiang.bruce
 * create time : 2011-10-20 下午03:56:06
 */
package com.shengpay.website.common.service;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 功能描述：
 * @author liuxiang.bruce
 * time : 2011-10-20 下午03:56:06
 */
public class DepositLimitResponse implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 8103837130982213069L;

    /**请求是否成功**/
    private boolean isSuccess;

    /**请求失败时候错误信息**/
    private String errorMessage;

    /**次数限制**/
    private boolean isTimesLimit;

    /**总金额限制**/
    private boolean isAmountLimit;

    /**已经充值的金额**/
    private BigDecimal depositeAmount;

    /**数据库规则中的限制次数**/
    private Long ruleTimes;

    /**数据库规则中的限制金额**/
    private Long ruleAmount;

    /**数据库规则中的统计时段**/
    private int validTimeType;

    public boolean isTimesLimit() {
        return isTimesLimit;
    }

    public boolean isAmountLimit() {
        return isAmountLimit;
    }

    public void setTimesLimit(boolean isTimesLimit) {
        this.isTimesLimit = isTimesLimit;
    }

    public void setAmountLimit(boolean isAmountLimit) {
        this.isAmountLimit = isAmountLimit;
    }

    public boolean isSuccess() {
        return isSuccess;
    }


    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Long getRuleTimes() {
        return ruleTimes;
    }

    public Long getRuleAmount() {
        return ruleAmount;
    }

    public void setRuleTimes(Long ruleTimes) {
        this.ruleTimes = ruleTimes;
    }

    public void setRuleAmount(Long ruleAmount) {
        this.ruleAmount = ruleAmount;
    }

    public int getValidTimeType() {
        return validTimeType;
    }

    public void setValidTimeType(int validTimeType) {
        this.validTimeType = validTimeType;
    }

    public BigDecimal getDepositeAmount() {
        return depositeAmount;
    }

    public void setDepositeAmount(BigDecimal depositeAmount) {
        this.depositeAmount = depositeAmount;
    }

}
