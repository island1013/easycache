package com.shengpay.website.common.service.util;

import java.net.URL;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;

import com.shengpay.commom.config.GlobalConfig;

/**
 *
 */

/**
 * 功能描述：
 * @author liuxiang.bruce
 * time : 2011-8-18 上午09:44:39
 */
public class MailUtil {

    /**
     * 
     * 
     * @param mailTo
     * @param body
     * @throws Throwable
     */
    public static void sendHtmlMail(String mailTo, String subject, String body) throws Throwable {
        MailInfo info = new MailInfo();
        String hostName = GlobalConfig.getString("smtpServer");
        String userName = GlobalConfig.getString("smtpUserName");
        String password = GlobalConfig.getString("smtpPassword");
        String mailFrom = GlobalConfig.getString("smtpSenderMail");
        String mailFromName = "盛付通";
        //        String subject = GlobalConfig.getString("smtpActiveTitle");
        info.setHostName(hostName);
        info.setUserName(userName);
        info.setPassword(password);
        info.setMailFrom(mailFrom);
        info.setMailFromName(mailFromName);
        info.setSubject(subject);
        info.setMailto(mailTo);
        //        info.setMailcc(mailTo);
        info.setContent(body);
        sendHtmlMail(info);
    }

    /**
     *
     * @param info
     * @throws Throwable
     */
    public static void sendHtmlMail(MailInfo info) throws Throwable {
        HtmlEmail email = new HtmlEmail();
        email.setHostName(info.getHostName()); //set host name
        if (info.isSSL()) {
            email.setSSL(true);
            if (null != info.getPort()) {
                email.setSslSmtpPort("" + info.getPort().intValue());
            }
        } else {
            if (null != info.getPort()) {
                email.setSmtpPort(info.getPort().intValue());
            }
        }
        if (null != info.getMailFromName()) {
            email.setFrom(info.getMailFrom(), info.getMailFromName());
        } else {
            email.setFrom(info.getMailFrom());
        }
        if (null != info.getCharset()) {
            email.setCharset(info.getCharset());
        } else {
            email.setCharset("UTF-8");
        }

        email.setAuthentication(info.getUserName(), info.getPassword());
        email.addTo(info.getMailto());
        if (null != info.getMailcc()) {
            email.addCc(info.getMailcc());
        }
        email.setSubject(info.getSubject());
        email.setHtmlMsg(info.getContent());

        email.send();

    }

    public static void sendHtmlMail() throws Throwable {
        HtmlEmail email = new HtmlEmail();
        email.setHostName("mail.sdo-service.com");
        email.setFrom("service@shengpay.com", "盛付通");
        email.addTo("liuxiang.bruce@snda.com", "尊敬的客户123");
        email.setSubject("盛付通钱包账户激活");
        email.setCharset("UTF-8");
        email.setAuthenticator(new DefaultAuthenticator("service@shengpay.com", "NJ4mQxaESpay"));

        // embed the image and get the content id
        URL url = new URL("http://www.apache.org/images/asf_logo_wide.gif");
        String cid = email.embed(url, "Apache logo");

        // set the html message
        //		  email.setHtmlMsg("<html>The apache logo 中文- <img src=\"cid:"+cid+"\"></html>");

        String imagUrl = "http://www.shengpay.com/Images/Mail/emailbg.jpg";
        String activeUrl = "http://www.shengpay.com/mailActive.htm?activecode=321637c9533a4cfae96007fc79f42c2c";
        StringBuffer sb = new StringBuffer();
        sb.append(
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">")
            .append("<html xmlns=\"http://www.w3.org/1999/xhtml\" >")
            .append("<head>")
            .append("<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />")
            .append("<style type=\"text/css\">")
            .append("body{ background-color:#fff; }")
            .append("div{ width:704px; height:auto; margin:16px auto; background:url(")
            .append(imagUrl)
            .append(") 0 44px no-repeat;}")
            .append("img{ width:164px; height:36px;}")
            .append(
                "h6{ width:640px; font-size:14px; font-weight:700; color:#000; margin:40px 32px 24px; }")
            .append(
                "p{ width:640px; font-size:12px; color:#000; margin:10px 32px; line-height:24px;}")
            .append(
                "p.tip{ width:608px; padding:4px 0 0 32px; border-top:dotted 1px #666; margin-top:32px; background:url($http://www.shengpay.com/Images/Mail$$/emailtip.png) 8px 8px no-repeat; color:#666;}")
            .append("a{ color:#06f; font-size:16px; font-weight:700;}")
            .append("span{ font-weight:700; margin:0 4px;}")
            .append("</style>")
            .append("    <title></title>")
            .append("</head>")
            .append("<body>")
            .append("<div>")
            .append(
                "<img alt=\"sdo.com\" src=\"http://www.shengpay.com/Images/Mail/emaillogo.png\" />")
            .append("<h6>尊敬的用户，您好!</h6>")
            .append("<p>您已申请盛付通钱包的激活，请点击下列链接，即可完成认证．如２４小时内，不做任何操作，本次申请无效．</p>")
            .append("<p><a href=\"")
            .append(activeUrl)
            .append("\">点击完成激活</a></p>")
            .append("<p><a href=\"")
            .append(activeUrl)
            .append("\">")
            .append(activeUrl)
            .append("</a></p>")
            .append("<p>如有任何问题，请拨打我们的客服热线<span>"+GlobalConfig.getString("customer.service.phone")+"</span>，谢谢！</p>")
            .append(
                "<p class=\"tip\">如果您的邮件阅读程序不支持点击，请将上面的地址拷贝至您的浏览器（例如IE）的地址栏后打开。 <br />这是一封自动生成的邮件，请勿直接回复本邮件。</p>")
            .append("</div>").append("</body>").append("</html>");
 

        email.setHtmlMsg(sb.toString());

        // set the alternative message
        email.setTextMsg("Your email client does not support HTML messages");
        // send the email
        email.send();

    }
    
    public static void sendSimpleHtmlMail(MailInfo info,String htmlContent) throws Throwable {    
        
        HtmlEmail email = new HtmlEmail();
        email.setHostName(info.getHostName()); //set host name
        if (info.isSSL()) {
            email.setSSL(true);
            if (null != info.getPort()) {
                email.setSslSmtpPort("" + info.getPort().intValue());
            }
        } else {
            if (null != info.getPort()) {
                email.setSmtpPort(info.getPort().intValue());
            }
        }
        if (null != info.getMailFromName()) {
            email.setFrom(info.getMailFrom(), info.getMailFromName());
        } else {
            email.setFrom(info.getMailFrom());
        }
        if (null != info.getCharset()) {
            email.setCharset(info.getCharset());
        } else {
            email.setCharset("UTF-8");
        }

        email.setAuthentication(info.getUserName(), info.getPassword());
        email.addTo(info.getMailto());
        if (null != info.getMailcc()) {
            email.addCc(info.getMailcc());
        }
        email.setSubject(info.getSubject()); 
 
        StringBuffer sb = new StringBuffer();
        sb
        .append(
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">")
        .append("<html xmlns=\"http://www.w3.org/1999/xhtml\" >")
        .append("<head>")
        .append("<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />")
        .append("<style type=\"text/css\">")
        .append("body{ background-color:#fff; }")
        .append("div{ width:704px; height:auto; margin:16px auto; background:url(")
        .append(") 0 44px no-repeat;}")
        .append("img{ width:164px; height:36px;}")
        .append("h6{ width:640px; font-size:14px; font-weight:700; color:#000; margin:40px 32px 24px; }")
        .append("p{ width:640px; font-size:12px; color:#000; margin:10px 32px; line-height:24px;}")
        .append(
                "p.tip{ width:608px; padding:4px 0 0 32px; border-top:dotted 1px #666; margin-top:32px; background:url($http://www.shengpay.com/Images/Mail$$/emailtip.png) 8px 8px no-repeat; color:#666;}")
        .append("a{ color:#06f; font-size:16px; font-weight:700;}")
        .append("span{ font-weight:700; margin:0 4px;}")
        .append("</style>")
        .append("    <title></title>")
        .append("</head>")
        .append("<body>")
        .append("<div>")
        .append("<img alt=\"sdo.com\" src=\"https://img0.shengpay.com/img/shengpay/CommonNew_img/shengpay_logo_01.png\" />")
            .append(htmlContent)    
            .append(
                "<p class=\"tip\">如果您的邮件阅读程序不支持点击，请将上面的地址拷贝至您的浏览器（例如IE）的地址栏后打开。 <br />这是一封自动生成的邮件，请勿直接回复本邮件。</p>")
            .append("</div>").append("</body>").append("</html>"); 

        email.setHtmlMsg(sb.toString());

        // set the alternative message
        email.setTextMsg("Your email client does not support HTML messages");
        // send the email
        email.send();       
    } 

    public static void main(String[] args) {
        try {
            sendHtmlMail();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
