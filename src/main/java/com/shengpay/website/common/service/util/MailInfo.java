/*
 * Copyright 2010 sdp.com, Inc. All rights reserved.
 * sdp.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * creator : liuxiang.bruce
 * create time : 2011-8-19 上午08:37:04
 */
package com.shengpay.website.common.service.util;

/**
 * 功能描述：
 * @author liuxiang.bruce
 * time : 2011-8-19 上午08:37:04
 */
public class MailInfo {
    private String hostName;
    private Integer port;
    private String userName;
    private String password;
    private boolean isSSL;
    private String charset;

    private String mailFrom;
    private String mailFromName;
    private String mailto;
    private String mailcc;
    private String subject;
    private String content;

    public String getHostName() {
        return hostName;
    }
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
    public Integer getPort() {
        return port;
    }
    public void setPort(Integer port) {
        this.port = port;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getMailFrom() {
        return mailFrom;
    }
    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }
    public String getMailFromName() {
        return mailFromName;
    }
    public void setMailFromName(String mailFromName) {
        this.mailFromName = mailFromName;
    }
    public String getMailto() {
        return mailto;
    }
    public void setMailto(String mailto) {
        this.mailto = mailto;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public boolean isSSL() {
        return isSSL;
    }
    public void setSSL(boolean isSSL) {
        this.isSSL = isSSL;
    }
    public String getCharset() {
        return charset;
    }
    public void setCharset(String charset) {
        this.charset = charset;
    }
    public String getMailcc() {
        return mailcc;
    }
    public void setMailcc(String mailcc) {
        this.mailcc = mailcc;
    }
}
