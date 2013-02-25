/*
 * Copyright 2010 sdp.com, Inc. All rights reserved.
 * sdp.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * creator : liuxiang.bruce
 * create time : 2011-10-20 下午04:11:59
 */
package com.shengpay.website.common.service.impl;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdo.ffs.service.facade.common.enums.StatisPeriodEnum;
import com.sdo.ffs.service.facade.query.FundsStatQueryService;
import com.sdo.ffs.service.facade.query.model.FundsStatQueryRequest;
import com.sdo.ffs.service.facade.query.model.FundsStatQueryResponse;
import com.sdo.ffs.service.facade.query.model.ProductPaymentPackage;
import com.shengpay.website.common.dal.daointerface.DepositLimitDAO;
import com.shengpay.website.common.dal.dataobject.DepositLimitDO;
import com.shengpay.website.common.service.DepositLimitResponse;
import com.shengpay.website.common.service.DepositeLimitService;

/**
 * 功能描述：
 * @author liuxiang.bruce
 * time : 2011-10-20 下午04:11:59
 */
@Service("depositeLimitService")
public class DepositeLimitServiceImpl implements DepositeLimitService {
    private static final Logger      logger   = LoggerFactory.getLogger(DepositeLimitServiceImpl.class);

    @Autowired
    private DepositLimitDAO depositLimitDAO;
    @Autowired
    private FundsStatQueryService fundsStatQueryService;

    /**
     * 组装数据请求包数据
     * @param productCode
     * @param depositCode
     * @param depositChannel
     * @return
     */
    private ProductPaymentPackage[] buildProductPaymentPackage(String productCode, String depositCode, String depositChannel){
        ProductPaymentPackage[] productGroups = null;

        int productCodeLength = -1;
        String[] productCodes = null;
        if(null != productCode){
            productCodes = productCode.split(",");
            productCodeLength = productCodes.length;
        }

        int depositCodeLength = -1;
        String[] depositCodes = null;
        if(null != depositCode){
            depositCodes = depositCode.split(",");
            depositCodeLength = depositCodes.length;
        }

        int depositChannelLength = -1;
        String[] depositChannels = null;
        if(null != depositChannel){
            depositChannels = depositChannel.split(",");
            depositChannelLength = depositChannels.length;
        }

        if(productCodeLength > 0){
            productGroups = new ProductPaymentPackage[productCodeLength];
        }
        if(null == productGroups){
            if(depositCodeLength > 0){
                productGroups = new ProductPaymentPackage[depositCodeLength];
            }
        }
        if(null == productGroups){
            if(depositChannelLength > 0){
                productGroups = new ProductPaymentPackage[depositChannelLength];
            }
        }

        if(null != productGroups){
            ProductPaymentPackage productPaymentPackage = null;
            for(int i=0; i<productGroups.length; i++){
                productPaymentPackage = new ProductPaymentPackage();
                if(null != productCodes){
                    if(!"".equals(productCodes[i].trim())){
                        productPaymentPackage.setProductCode(productCodes[i]);
                    }
                }
                if(null != depositCodes){
                    if(!"".equals(depositCodes[i].trim())){
                        productPaymentPackage.setPaymentCode(depositCodes[i]);
                    }
                }
                if(null != depositChannels){
                    if(!"".equals(depositChannels[i].trim())){
                        productPaymentPackage.setChannelModes(new String[]{depositChannels[i]});
                    }
                }
                productGroups[i] = productPaymentPackage;
            }
        }

        return productGroups;
    }

    @Override
    public DepositLimitResponse queryLimit(String ptid, String ruleID){
        return queryLimit(ptid, ruleID, null);
    }

    @Override
    public DepositLimitResponse queryLimit(String ptid, String ruleID, BigDecimal currentDepositAmount) {
        DepositLimitResponse response = new DepositLimitResponse();

        DepositLimitDO limitDO = null;
        try{
            limitDO = depositLimitDAO.queryLimitRecordByRule(ruleID);
            if(null != limitDO){
                String productCode = limitDO.getProductCode();          //产品编码
                String depositCode = limitDO.getDepositCode();          //支付编码
                String depositChannel = limitDO.getDepositChannel();    //支付渠道
                Long validTimeType = limitDO.getValidTime();            //统计日期类型    0当日  1当月  2当年
                if(null != validTimeType){
                    response.setValidTimeType(validTimeType.intValue());
                }

                //请求风控的服务接口
                FundsStatQueryRequest request = new FundsStatQueryRequest();
                request.setMemberId(ptid);
                request.setRulePackageId(2L);
                ProductPaymentPackage[] productGroups = buildProductPaymentPackage(productCode, depositCode, depositChannel);
                request.setProductGroup(productGroups);

                StatisPeriodEnum requestStatisPeriodEnum = null;
                if(0L == validTimeType){
                    requestStatisPeriodEnum = StatisPeriodEnum.DAY;     //统计当日
                }else if(1L == validTimeType){
                    requestStatisPeriodEnum = StatisPeriodEnum.MONTH;   //统计当月
                }else if(2L == validTimeType){
                    requestStatisPeriodEnum = StatisPeriodEnum.YEAR;    //统计当年
                }
                request.setPeriod(requestStatisPeriodEnum);

                String sourceCode = "442";
                request.setSourceCode(sourceCode);
                FundsStatQueryResponse fundsStatQueryResponse = fundsStatQueryService.query(request);
                int limitTimes = -1;
                BigDecimal limitAmount = null;
                if(null != fundsStatQueryResponse){
                    String returnCode = fundsStatQueryResponse.getReturnCode();
                    if(null != returnCode){
                        if("0000".equals(returnCode) || "0002".equals(returnCode)){
                            try{
                                limitTimes = fundsStatQueryResponse.getStatTimes();
                                limitAmount = new BigDecimal(fundsStatQueryResponse.getStatResult());
                                response.setDepositeAmount(limitAmount);
                            }catch(Throwable t){
                                logger.error("Execute limit amount from FundsStatQueryService.query() make error!", t);
                            }
                        }
                    }

                }

                Long timesRule = limitDO.getDepositTimes();
                Long amountRule = limitDO.getDepositAmount();

                if(null != timesRule){
                    response.setRuleTimes(timesRule);
                    if(limitTimes >= timesRule){
                        response.setTimesLimit(Boolean.TRUE);
                    }
                }
                if(null != amountRule){
                    response.setRuleAmount(amountRule);
                    if(null != limitAmount){
                        if(currentDepositAmount != null){
                            //规则当前的充值金额+已经充值的金额 是否大于规则定的金额
                            limitAmount = limitAmount.add(currentDepositAmount);
                        }
                        if(limitAmount.doubleValue() > amountRule){
                            response.setAmountLimit(Boolean.TRUE);
                        }
                    }
                }
                response.setSuccess(Boolean.TRUE);
            }else{
                response.setSuccess(Boolean.FALSE);
                response.setErrorMessage("By ruleID[" + ruleID + "] query db is null.");
            }
        }catch(Throwable t){
            response.setSuccess(Boolean.FALSE);
            response.setErrorMessage("Not found ruleID [" + ruleID + "].");
            logger.error("execute query depositlimit make error!", t);
        }

        return response;
    }

    public DepositLimitDAO getDepositLimitDAO() {
        return depositLimitDAO;
    }

    public void setDepositLimitDAO(DepositLimitDAO depositLimitDAO) {
        this.depositLimitDAO = depositLimitDAO;
    }


    public FundsStatQueryService getFundsStatQueryService() {
        return fundsStatQueryService;
    }


    public void setFundsStatQueryService(FundsStatQueryService fundsStatQueryService) {
        this.fundsStatQueryService = fundsStatQueryService;
    }

    public static void main(String[] args) {
        DepositeLimitServiceImpl test = new DepositeLimitServiceImpl();
        String productCode = "10010001,10010001";          //产品编码
        String depositCode = "1001,2001";          //支付编码
        String depositChannel = "27,35";    //支付渠道
        ProductPaymentPackage[] productGroups = test.buildProductPaymentPackage(productCode, depositCode, depositChannel);
        System.out.println(productCode);

        productGroups = test.buildProductPaymentPackage(productCode, depositCode, "27, ");
        System.out.println(productCode);
    }

}
