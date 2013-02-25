package com.shengpay.website.common.service.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.sun.crypto.provider.SunJCE;

public class ActiveXCryptUtil {

    private static final int SALT_LEN = 2;
    private static final int ZERO_LEN = 7;

    private static byte[]    codes    = null;

    private static byte[] getCodes() {
        if (codes == null) {
            codes = new byte[256];
            for (int i = 0; i < 256; i++)
                codes[i] = -1;
            for (int i = 'A'; i <= 'Z'; i++)
                codes[i] = (byte) (i - 'A');
            for (int i = 'a'; i <= 'z'; i++)
                codes[i] = (byte) (26 + i - 'a');
            for (int i = '0'; i <= '9'; i++)
                codes[i] = (byte) (52 + i - '0');
            codes['+'] = 62;
            codes['/'] = 63;
        }
        return codes;
    }

    private static byte[] base64decode(char[] data) {

        byte[] codes = getCodes();

        int len = ((data.length + 3) / 4) * 3;
        if (data.length > 0 && data[data.length - 1] == '=')
            --len;
        if (data.length > 1 && data[data.length - 2] == '=')
            --len;
        byte[] out = new byte[len];
        int shift = 0;
        int accum = 0;
        int index = 0;
        for (int ix = 0; ix < data.length; ix++) {
            int value = codes[data[ix] & 0xFF];
            if (value >= 0) {
                accum <<= 6;
                shift += 6;
                accum |= value;
                if (shift >= 8) {
                    shift -= 8;
                    out[index++] = (byte) ((accum >> shift) & 0xff);
                }
            }
        }
        if (index != out.length)
            throw new Error("miscalculated data length!");
        return out;
    }

    private static class pOutBufLenClass {

        private int length = 1024;

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }
    }

    /*pKey为16byte*/
    /*
    	输入:pInBuf为密文格式,nInBufLen为pInBuf的长度是8byte的倍数; *pOutBufLen为接收缓冲区的长度
    		特别注意*pOutBufLen应预置接收缓冲区的长度!
    	输出:pOutBuf为明文(Body),pOutBufLen为pOutBuf的长度,至少应预留nInBufLen-10;
    	返回值:如果格式正确返回TRUE;
    */
    /*TEA解密算法,CBC模式*/
    /*密文格式:PadLen(1byte)+Padding(var,0-7byte)+Salt(2byte)+Body(var byte)+Zero(7byte)*/

    private static boolean oi_symmetry_decrypt2(byte[] pInBuf, int nInBufLen, long[] pKey,
                                                byte[] pOutBuf, pOutBufLenClass pOutBufLenClass) {

        int nPadLen, nPlainLen;
        byte[] dest_buf = new byte[8], zero_buf = new byte[8];
        byte[] iv_pre_crypt, iv_cur_crypt;
        int pre_index = 0, cur_index = 0, in_index = 0;
        int dest_i, i, j;
        int nBufPos;
        nBufPos = 0;

        if ((nInBufLen % 8 > 0) || (nInBufLen < 16))
            return false;

        TeaDecryptECB(pInBuf, pKey, dest_buf);

        nPadLen = dest_buf[0] & 0x7/*只要最低三位*/;

        /*密文格式:PadLen(1byte)+Padding(var,0-7byte)+Salt(2byte)+Body(var byte)+Zero(7byte)*/
        i = nInBufLen - 1/*PadLen(1byte)*/- nPadLen - SALT_LEN - ZERO_LEN; /*明文长度*/

        if ((pOutBufLenClass.getLength() < i) || (i < 0))
            return false;
        pOutBufLenClass.setLength(i);

        for (i = 0; i < 8; i++)
            zero_buf[i] = 0;

        iv_pre_crypt = zero_buf;
        iv_cur_crypt = pInBuf; /*init iv*/
        pre_index = 0;
        cur_index = in_index;

        in_index += 8;
        nBufPos += 8;

        dest_i = 1; /*dest_i指向dest_buf下一个位置*/

        /*把Padding滤掉*/
        dest_i += nPadLen;

        /*dest_i must <=8*/

        /*把Salt滤掉*/
        for (i = 1; i <= SALT_LEN;) {
            if (dest_i < 8) {
                dest_i++;
                i++;
            } else if (dest_i == 8) {
                /*解开一个新的加密块*/

                /*改变前一个加密块的指针*/
                iv_pre_crypt = iv_cur_crypt;
                iv_cur_crypt = pInBuf;
                pre_index = cur_index;
                cur_index = in_index;

                /*异或前一块明文(在dest_buf[]中)*/
                for (j = 0; j < 8; j++) {
                    if ((nBufPos + j) >= nInBufLen)
                        return false;
                    dest_buf[j] ^= pInBuf[j + in_index];
                }

                /*dest_i==8*/
                TeaDecryptECB(dest_buf, pKey, dest_buf);

                /*在取出的时候才异或前一块密文(iv_pre_crypt)*/

                in_index += 8;
                nBufPos += 8;

                dest_i = 0; /*dest_i指向dest_buf下一个位置*/
            }
        }

        /*还原明文*/

        int outIndex = 0;
        nPlainLen = pOutBufLenClass.getLength();
        while (nPlainLen > 0) {
            if (dest_i < 8) {
                pOutBuf[outIndex] = (byte) (dest_buf[dest_i] ^ iv_pre_crypt[dest_i + pre_index]);
                outIndex++;
                dest_i++;
                nPlainLen--;
            } else if (dest_i == 8) {
                /*dest_i==8*/

                /*改变前一个加密块的指针*/
                iv_pre_crypt = iv_cur_crypt;
                iv_cur_crypt = pInBuf;
                pre_index = cur_index;
                cur_index = in_index;

                /*解开一个新的加密块*/

                /*异或前一块明文(在dest_buf[]中)*/
                for (j = 0; j < 8; j++) {
                    if ((nBufPos + j) >= nInBufLen)
                        return false;
                    dest_buf[j] ^= pInBuf[j + in_index];
                }

                TeaDecryptECB(dest_buf, pKey, dest_buf);

                /*在取出的时候才异或前一块密文(iv_pre_crypt)*/

                in_index += 8;
                nBufPos += 8;

                dest_i = 0; /*dest_i指向dest_buf下一个位置*/
            }
        }

        /*校验Zero*/
        for (i = 1; i <= ZERO_LEN;) {
            if (dest_i < 8) {
                if ((dest_buf[dest_i] ^ iv_pre_crypt[dest_i + pre_index]) > 0)
                    return false;
                dest_i++;
                i++;
            } else if (dest_i == 8) {
                /*改变前一个加密块的指针*/
                iv_pre_crypt = iv_cur_crypt;
                iv_cur_crypt = pInBuf;
                pre_index = cur_index;
                cur_index = in_index;

                /*解开一个新的加密块*/

                /*异或前一块明文(在dest_buf[]中)*/
                for (j = 0; j < 8; j++) {
                    if ((nBufPos + j) >= nInBufLen)
                        return false;
                    dest_buf[j] ^= pInBuf[j + in_index];
                }

                TeaDecryptECB(dest_buf, pKey, dest_buf);

                /*在取出的时候才异或前一块密文(iv_pre_crypt)*/

                in_index += 8;
                nBufPos += 8;
                dest_i = 0; /*dest_i指向dest_buf下一个位置*/
            }

        }

        return true;
    }

    //若某字节被解释成负的则需将其转成无符号正数
    private static long transform(byte temp) {
        long tempInt = (long) temp;
        if (tempInt < 0) {
            tempInt += 256L;
        }
        return tempInt;
    }

    private static void TeaDecryptECB(byte[] pInBuf, long[] pKey, byte[] pOutBuf) {
        long y, z, sum = 0L;
        int i;

        /*now encrypted buf is TCP/IP-endian;*/
        /*TCP/IP network byte order (which is big-endian).*/
        y = mybyteToInt(pInBuf, 0);
        z = mybyteToInt(pInBuf, 4);

        long DELTA = 0x9e3779b9L;
        sum = (DELTA * 16) & 0xffffffffL;

        for (i = 0; i < 16; i++) {
            z -= ((y * 16) & 0xffffffffL) + pKey[2] ^ y + sum ^ ((y & 0xffffffffL) / 32) + pKey[3];
            y -= ((z * 16) & 0xffffffffL) + pKey[0] ^ z + sum ^ ((z & 0xffffffffL) / 32) + pKey[1];
            sum -= DELTA;
        }

        myintToByte(y, pOutBuf, 0);
        myintToByte(z, pOutBuf, 4);

        /*now plain-text is TCP/IP-endian;*/
    }

    private static long mybyteToInt(byte[] source, int offset) {

        return (transform(source[offset])) << 24 | (transform(source[offset + 1])) << 16
               | (transform(source[offset + 2])) << 8 | transform(source[offset + 3]);
    }

    private static void myintToByte(long value, byte[] target, int offset) {
        target[offset] = (byte) ((value & 0xff000000) >> 24);
        target[offset + 1] = (byte) ((value & 0xff0000) >> 16);
        target[offset + 2] = (byte) ((value & 0xff00) >> 8);
        target[offset + 3] = (byte) (value & 0xff);
    }

    private static long[] toKey(String pkey) {
        long[] result = new long[4];

        for (int i = 0; i < 16; i += 4) {
            String sub = pkey.substring(i, i + 4);

            result[i / 4] = mybyteToInt(sub.getBytes(), 0);
        }
        return result;
    }

    private static String desAlgorithm = "DESede/CBC/PKCS7Padding";
    private static byte   defaultIV[]  = { 50, 51, 52, 53, 54, 55, 56, 57 };

    /*
     * ------------------3des加解密-----------------------------
     */
    static {

        try {
            Security.addProvider(new SunJCE());
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());//添加PKCS7Padding支持
        } catch (Exception e) {
            throw new RuntimeException("加载SunJCE出错");
        }
    }

    /**
     *
     * 生成密钥key对象
     * @param KeyStr 密钥字符串
     * @return 密钥对象
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws Exception
     */
    private static Key KeyGenerator(byte input[]) throws InvalidKeyException,
                                                 NoSuchAlgorithmException, InvalidKeySpecException {

        Key key = new SecretKeySpec(input, desAlgorithm);
        return key;
    }

    /**
     *
     * Des加密解密核心方法
     * @param input 需要加密或者解密的字节数组
     * @param key   密钥
     * @param algorithm 算法
     * @param cryptModel 加密模式 1加密 2解密
     * @param iv 加密向量 可以为空
     * @return 加密或者解密的字节数组
     */
    private static byte[] cryptBy3Des(byte input[], byte key[], int cryptModel, byte iv[])
                                                                                          throws Exception {
        try {
            Key k = KeyGenerator(key);
            IvParameterSpec IVSpec = iv != null ? IvGenerator(iv) : IvGenerator(defaultIV);
            Cipher c = Cipher.getInstance(desAlgorithm);
            c.init(cryptModel, k, ((java.security.spec.AlgorithmParameterSpec) (IVSpec)));
            return c.doFinal(input);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 3DES加密
     * @param input 需要加密的内容
     * @param key   密钥
     * @return
     */
    private static byte[] encryptBy3Des(byte input[], byte key[]) throws Exception {

        try {
            return cryptBy3Des(input, key, 1, null);
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    }

    /**
     * 3DES解密
     * @param input 需要解密的内容
     * @param key   密钥
     * @return
     */
    private static byte[] decryptBy3Des(byte input[], byte[] key) throws Exception {
        try {
            return cryptBy3Des(input, key, 2, null);
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    }

    private static IvParameterSpec IvGenerator(byte[] b) {
        IvParameterSpec IV = new IvParameterSpec(b);
        return IV;
    }

    public static String ActiveXDecrypt(String src, String key) throws Exception {
        try {

            /*
             * ----base64解密-----------------------
             */
            byte[] test1 = base64decode(src.toCharArray());

            byte[] result = new byte[1024];

            pOutBufLenClass pOutBufLenClass = new pOutBufLenClass();

            pOutBufLenClass.setLength(1024);

            /*
             * ---------tea解密-----------------------
             */
            oi_symmetry_decrypt2(test1, test1.length, toKey(key), result, pOutBufLenClass);

            byte[] temp = new byte[pOutBufLenClass.getLength()];

            System.arraycopy(result, 0, temp, 0, temp.length);

            byte[] keyBytes = new byte[24];
            byte[] destBytes = new byte[pOutBufLenClass.getLength() - 8];

            System.arraycopy(temp, 0, destBytes, 0, pOutBufLenClass.getLength() - 8);
            System.arraycopy(temp, pOutBufLenClass.getLength() - 8, keyBytes, 0, 8);

            for (int i = 8; i < 24; i++) {
                keyBytes[i] = (byte) 8;
            }

            byte[] srcBytes = null;

            /*
             * ---------3des解密-----------------------
             */
            srcBytes = decryptBy3Des(destBytes, keyBytes);

            return new String(srcBytes);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw e;
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {

        String dest = "rbJnYJISkbpkzFkqM881vCBKmdvaGVGcUGdfJRSdXpmH7KqXf6EOlA==";

        String pkey = "0123456789abcdef";

        try {
            String temp = ActiveXCryptUtil.ActiveXDecrypt(dest, pkey);

            System.out.println("result string:" + new String(temp));
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}