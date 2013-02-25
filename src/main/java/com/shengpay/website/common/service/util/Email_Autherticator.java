package com.shengpay.website.common.service.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 *
 * 功能描述：
 * @author liuxiang.bruce
 * time : 2011-8-18 上午09:55:17
 */
class Email_Autherticator extends Authenticator{
    private String username;
    private String password;
    public Email_Autherticator(){
        super();
    }

    public Email_Autherticator(String user, String pwd){
        super();
        username = user;
        password = pwd;
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication()
    {
        return new PasswordAuthentication(username, password);
    }
}
