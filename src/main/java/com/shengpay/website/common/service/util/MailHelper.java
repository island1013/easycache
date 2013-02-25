/*
 * Copyright 2010 sdp.com, Inc. All rights reserved.
 * sdp.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * creator : liuxiang.bruce
 * create time : 2011-8-19 上午08:54:23
 */
package com.shengpay.website.common.service.util;

import java.io.StringWriter;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.shengpay.commom.config.GlobalConfig;

/**
 * 功能描述：
 * @author liuxiang.bruce
 * time : 2011-8-19 上午08:54:23
 */
public class MailHelper {

    /**
     *
     * @param mailTo
     * @param macString
     * @throws Throwable
     */
    public static void sendMail(String mailTo, String macString) throws Throwable{
        String body = parserContent(macString);
        sendMailInit(mailTo, body);
    }

    /**
     *
     * @param macString
     * @return
     */
    private static String parserContent(String macString){
        String imagUrl = GlobalConfig.getString("domain.global") + "Images/Mail/emailbg.jpg";
        String emaillogoUrl = GlobalConfig.getString("domain.global") + "Images/Mail/emaillogo.png";
        String activeUrl = GlobalConfig.getString("sfpayMailActiveUrl") + macString;

        StringBuffer sb = new StringBuffer();
        sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">")
        .append("<html xmlns=\"http://www.w3.org/1999/xhtml\" >")
        .append("<head>")
        .append("<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />")
        .append("<style type=\"text/css\">")
        .append("body{ background-color:#fff; }")
        .append("div{ width:704px; height:auto; margin:16px auto; background:url(").append(imagUrl).append(") 0 44px no-repeat;}")
        .append("img{ width:164px; height:36px;}")
        .append("h6{ width:640px; font-size:14px; font-weight:700; color:#000; margin:40px 32px 24px; }")
        .append("p{ width:640px; font-size:12px; color:#000; margin:10px 32px; line-height:24px;}")
        .append("p.tip{ width:608px; padding:4px 0 0 32px; border-top:dotted 1px #666; margin-top:32px; background:url($http://www.shengpay.com/Images/Mail$$/emailtip.png) 8px 8px no-repeat; color:#666;}")
        .append("a{ color:#06f; font-size:16px; font-weight:700;}")
        .append("span{ font-weight:700; margin:0 4px;}")
        .append("</style>")
        .append("    <title></title>")
        .append("</head>")
        .append("<body>")
        .append("<div>")
        .append("<img alt=\"sdo.com\" src=" + emaillogoUrl + " />")
        .append("<h6>尊敬的用户，您好!</h6>")
        .append("<p>您已申请盛付通钱包的激活，请点击下列链接，即可完成认证．如２４小时内，不做任何操作，本次申请无效．</p>")
        .append("<p><a href=\"").append(activeUrl).append("\">点击完成激活</a></p>")
        .append("<p><a href=\"").append(activeUrl).append("\">").append(activeUrl).append("</a></p>")
        .append("<p>如有任何问题，请拨打我们的客服热线<span>"+GlobalConfig.getString("customer.service.phone")+"</span>，谢谢！</p>")
        .append("<p class=\"tip\">如果您的邮件阅读程序不支持点击，请将上面的地址拷贝至您的浏览器（例如IE）的地址栏后打开。 <br />这是一封自动生成的邮件，请勿直接回复本邮件。</p>")
        .append("</div>")
        .append("</body>")
        .append("</html>");


        String content = sb.toString();


//        try {
//            VelocityEngine ve = new VelocityEngine();
//            Properties properties = new Properties();
////            properties.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, basePath);
//            properties.setProperty("file.resource.loader.class" , "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader" );
//            properties.setProperty("input.encoding", "UTF-8");
//            properties.setProperty("output.encoding", "UTF-8");
//            ve.init(properties);
//            Template t = ve.getTemplate("META-INF/activeMailContent.vm");
//            VelocityContext context = new VelocityContext();
//            context.put("IMGURL", imagUrl);
//            context.put("ACTIVEURL", activeUrl);
//            StringWriter writer = new StringWriter();
//            t.merge(context, writer);
//            content = writer.toString();
//        } catch (Throwable t) {
//            t.printStackTrace();
//        }
        return content;
    }

    /**
     *
     * @param mailTo
     * @param body
     * @throws Throwable
     */
    private static void sendMailInit(String mailTo, String body) throws Throwable{
        MailInfo mailInfo = buildMailInfo(mailTo, body);
        MailUtil.sendHtmlMail(mailInfo);
    }

    /**
     *
     * @param mailTo
     * @param body
     * @return
     */
    private static MailInfo buildMailInfo(String mailTo, String body){
        MailInfo info = new MailInfo();
        String hostName = GlobalConfig.getString("smtpServer");
        String userName = GlobalConfig.getString("smtpUserName");
        String password = GlobalConfig.getString("smtpPassword");
        String mailFrom = GlobalConfig.getString("smtpSenderMail");
        String mailFromName = "盛付通";
//        String subject = GlobalConfig.getString("smtpActiveTitle");
        String subject = "盛付通钱包账户激活";
        info.setHostName(hostName);
        info.setUserName(userName);
        info.setPassword(password);
        info.setMailFrom(mailFrom);
        info.setMailFromName(mailFromName);
        info.setSubject(subject);
        info.setMailto(mailTo);
//        info.setMailcc(mailTo);
        info.setContent(body);
        return info;
    }

    public static void main(String[] args) {
        try {
            VelocityEngine ve = new VelocityEngine();
            Properties properties = new Properties();
//            String basePath = "E:/work/website/website-website/src/main/webapp/view/";
//            properties.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, basePath);
            properties.setProperty("file.resource.loader.class" ,  "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader" );
            properties.setProperty("input.encoding", "UTF-8");
            properties.setProperty("output.encoding", "UTF-8");
            ve.init(properties);
            Template t = ve.getTemplate("com/shengpay/website/action/util/activeMailContent.vm");
            VelocityContext context = new VelocityContext();
            context.put("IMGURL", "imgurl");
            context.put("ACTIVEURL", "activeUrl");
            StringWriter writer = new StringWriter();
            t.merge(context, writer);
            System.out.println(writer.toString());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
