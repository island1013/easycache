/*
 * Copyright 2010 sdp.com, Inc. All rights reserved.
 * sdp.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * creator : liuxiang.bruce
 * create time : 2011-11-21 下午02:00:04
 */
package com.shengpay.website.common.service.impl;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shengpay.website.common.dal.daointerface.AuthenticationDAO;
import com.shengpay.website.common.dal.dataobject.AuthenticationDO;
import com.shengpay.website.common.service.SdoNotifyService;

/**
 * 功能描述：
 * @author liuxiang.bruce
 * time : 2011-11-21 下午02:00:04
 */
@Service("sdoNotifyService")
public class SdoNotifyServiceImpl implements SdoNotifyService {
    @Autowired
    private AuthenticationDAO authenticationDAO;

    @Override
    public boolean activeNotify(String ptid, String mobile) {
        if(null == ptid){
            return false;
        }
        AuthenticationDO authenticationDO = authenticationDAO.queryRecordByPtid(ptid);
        if(null == authenticationDO){
            authenticationDO = new AuthenticationDO();
            authenticationDO.setPt(ptid);
            authenticationDO.setInserttime(new Date());
            authenticationDO.setSyncstatus(0);
            authenticationDO.setStatus(0);
            if(null != mobile){
            	authenticationDO.setCellphone(mobile);
            }
            authenticationDAO.insertAuthentication(authenticationDO);
        }else{
            authenticationDAO.updateAuthenticationWhenActive(ptid);
        }
        return false;
    }

}
