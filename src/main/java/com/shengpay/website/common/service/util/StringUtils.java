/*
 * Copyright 2010 sdp.com, Inc. All rights reserved.
 * sdp.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * creator : wangxinyou.jason
 * create time : 2011-9-5 下午07:16:13
 */
package com.shengpay.website.common.service.util;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 功能描述：字符串处理服务
 * @author wangxinyou.jason
 * time : 2011-9-5 下午07:16:13
 */
public class StringUtils {

    public static List<String> imgList = new ArrayList<String>();

    static{
        imgList.add("jpg");
        imgList.add("jpeg");
        imgList.add("png");
        imgList.add("bmp");
        imgList.add("gif");
    }
    /**
     * 转化银行卡
     * @param bankCardNo
     * @return
     */
    public static String transfBankCardNo(String bankCardNo){
        if(null == bankCardNo || "".equals(bankCardNo)){
            return "";
        }
        int len = bankCardNo.length();
        StringBuilder buf = new StringBuilder();
        if(len >= 16){
            buf.append(bankCardNo.substring(0,4));
            buf.append(" **** **** ");
            buf.append(bankCardNo.substring(len-4,len));
            return buf.toString();
        }else{
            return bankCardNo;
        }
    }

    /**
     * 转化身份证号码
     * @param bankCardNo
     * @return
     */
    public static String transfIdCardNo(String idCardNo){
        if(null == idCardNo || "".equals(idCardNo)){
            return "";
        }
        int len = idCardNo.length();
        StringBuilder buf = new StringBuilder();
        if(len >= 15){
            buf.append(idCardNo.substring(0,4));
            buf.append(" **** **** ");
            buf.append(idCardNo.substring(len-3,len));
            return buf.toString();
        }else{
            return idCardNo;
        }
    }

    /**
     * 去除空格
     * @param str
     * @return
     */
    public static String nullTrim(String str){
        if(null == str){
            return "";
        }else{
            return str.trim();
        }
    }

    /**
     * 转化为UTF-8编码
     * @param str
     * @return
     */
    public static String transfToUtf8(String str){
        String temp = nullTrim(str);
        /*try {
            return new String(temp.getBytes(),"utf-8");
        } catch (UnsupportedEncodingException e) {
            return temp;
        }*/
        return temp;
    }

    /**
     * 获取图片后缀
     * @param str
     * @return
     */
    public static String getPicSuffix(String str){
        String temp = nullTrim(str);
        int index = temp.lastIndexOf(".");
        if(-1 == index){
            return "";
        }else{
            return temp.substring(index+1,temp.length());
        }
    }

    public static boolean validIdCardNo(String idCardNo){
        if(null == idCardNo || "".equals(idCardNo)){
            return false;
        }
        if (!idCardNo.matches("^\\d{15}(\\d{2}[0-9Xx])?$")) {
            return false;
        }

        if (idCardNo.length() == 15) {
            Date n = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(n);
            int year = c.get(Calendar.YEAR);
            if (Integer.parseInt("19" + idCardNo.substring(6, 8)) < 1900 || Integer.parseInt("19" + idCardNo.substring(6, 8)) > year) {
                return false;
            }

            String birth = "19" + idCardNo.substring(6, 8) + "-" + idCardNo.substring(8, 10) + "-" + idCardNo.substring(10, 12);
            if (!birth.matches("^(\\d{4})-(0\\d{1}|1[0-2])-(0\\d{1}|[12]\\d{1}|3[01])")) {
               return false;
           }
        }
        if (idCardNo.length() == 18) {
            Date n = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(n);
            int year = c.get(Calendar.YEAR);
            if (Integer.parseInt(idCardNo.substring(6, 10)) < 1900 || Integer.parseInt(idCardNo.substring(6, 10)) > year) {
                return false;
            }

           String birth = idCardNo.substring(6, 10) + "-" + idCardNo.substring(10, 12) + "-" + idCardNo.substring(12, 14);
            if (!birth.matches("^(\\d{4})-(0\\d{1}|1[0-2])-(0\\d{1}|[12]\\d{1}|3[01])")) {
                return false;
           }


            int[] iW = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1};

            int iSum = 0;
            int iVal = 0;
            for (int i = 0; i < 17; i++) {
                char iC = idCardNo.charAt(i);
                iVal = Integer.parseInt("" +iC);
                iSum += iVal * iW[i];
            }

            int iJYM = iSum % 11;
            String sJYM = "";
            if (iJYM == 0) {
                sJYM = "1";
            } else if (iJYM == 1) {
                sJYM = "0";
            } else if (iJYM == 2) {
                sJYM = "x";
            } else if (iJYM == 3) {
                sJYM = "9";
            } else if (iJYM == 4) {
                sJYM = "8";
            } else if (iJYM == 5) {
                sJYM = "7";
            } else if (iJYM == 6) {
                sJYM = "6";
            } else if (iJYM == 7) {
                sJYM = "5";
            } else if (iJYM == 8) {
                sJYM = "4";
            } else if (iJYM == 9) {
                sJYM = "3";
            } else if (iJYM == 10) {
                sJYM = "2";
            }

            String cCheck = String.valueOf(idCardNo.charAt(17)).toLowerCase();
            if (!cCheck.equals(sJYM)) {
                return false;
            }
        }

        return true;
    }

    public static int transfToNum(String amt){
        if(null == amt || "".equals(amt)){
            return 0;
        }
        Float f = Float.valueOf(amt);
        return (int)(f*100);
    }

    /**
     * 中文姓名加码
     * @param name
     * @return
     */
    public static String transfChinaName(String name){
        if(null == name){
            return "";
        }
        int len = name.length();
        if(len >= 1){
            return name.substring(0,1) + "**";
        }else{
            return name;
        }
    }

    public static String transfLoginName(String name){
        if(null == name){
            return "";
        }
        int len = name.length();
        if(len >= 2){
            return name.substring(0,2) + "****";
        }else{
            return name;
        }
    }

    /**
     * 获取MD5字符串
     * @param str
     * @return
     */
    public static String strToMD5Str(String str){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] newBytes = md.digest();
            return hexString(newBytes);
        }catch(Exception e){
            return "";
        }
    }

    public static String hexString(byte[] b) {
        StringBuffer d = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; ++i) {
            char hi = Character.forDigit(b[i] >> 4 & 0xF, 16);
            char lo = Character.forDigit(b[i] & 0xF, 16);
            d.append(Character.toUpperCase(hi));
            d.append(Character.toUpperCase(lo));
        }
        return d.toString();
    }

    public static String tranfStr(String str){
        if(null == str){
            return "";
        }
        return str.trim();
    }

    public static void main(String[] args){
        System.out.println(getPicSuffix(""));
        System.out.println("sdfsdDs".toLowerCase());
        System.out.println(validIdCardNo("432522198308027394"));
        System.out.println(transfToNum("0.2"));
    }
}
