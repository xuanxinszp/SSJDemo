package com.xuanxinszp.ssjdemo.common.util;

import com.google.common.io.BaseEncoding;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 字符串工具
 * Created by Benson on 2018/3/28.
 */
public class StringUtil {

    public static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w\\.\\-]+@[\\w\\.\\-]+(\\.[\\w\\-]{2,3}){1,2}$");

    public static final Pattern MOBILE_PATTERN = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(17[0,6-8])|(18[0-9]))\\d{8}$");

    public static final char[] charMap;
    static {
        charMap = new char[64];
        for (int i = 0; i < 10; i++) {
            charMap[i] = (char) ('0' + i);
        }
        for (int i = 10; i < 36; i++) {
            charMap[i] = (char) ('a' + i - 10);
        }
        for (int i = 36; i < 62; i++) {
            charMap[i] = (char) ('A' + i - 36);
        }
        charMap[62] = '_';
        charMap[63] = '-';
    }

    /**
     * 判空
     */
    public static boolean isNil(String string) {
        return string == null || string.length() == 0;
    }

    /**
     * 判非空
     */
    public static boolean isNotNil(String string) {
        return !isNil(string);
    }
    /**
     * 判空白字符串
     */
    public static boolean isBlank(String str) {
        return isNil(str) || 0 == str.trim().length();
    }

    /**
     * 判空白字符串
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean isMobile(String input) {
        return MOBILE_PATTERN.matcher(input).matches();
    }

    public static String base64(String string) {
        return base64(string.getBytes());
    }

    public static String base64(byte[] bs) {
        return BaseEncoding.base64().encode(bs);
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取更短的UUID，长度为22
     */
    public static String get64UUID() {
        String uuid = "0" + getUUID();
        return hexTo64(uuid);
    }

    private static String hexTo64(String hex) {
        StringBuffer r = new StringBuffer();
        int index = 0;
        int[] buff = new int[3];
        int l = hex.length();
        for (int i = 0; i < l; i++) {
            index = i % 3;
            buff[index] = Integer.parseInt("" + hex.charAt(i), 16);
            if (index == 2) {
                r.append(charMap[buff[0] << 2 | buff[1] >>> 2]);
                r.append(charMap[(buff[1] & 3) << 4 | buff[2]]);
            }
        }
        return r.toString();
    }


    /**
     * 字符串转字符数组
     *
     * @param str
     * @param radix 多少进制
     * @return
     */
    public static byte[] toBytes(String str, int radix) {
        byte[] srcBytes = str.getBytes();

        // 2个16进制字符占用1个字节，8个二进制字符占用1个字节
        int size = 2;
        if (radix == 2) {
            size = 8;
        }

        byte[] tgtBytes = new byte[srcBytes.length / size];
        for (int i = 0; i < srcBytes.length; i = i + size) {
            String tmp = new String(srcBytes, i, size);
            tgtBytes[i / size] = (byte) Integer.parseInt(tmp, radix);
        }
        return tgtBytes;
    }

    /**
     * string数组转换int数组
     */
    public static int[] toIntegers(String... strs){
        int[] intArr = new int[strs.length];
        for(int i =0;i<strs.length;i++){
            intArr[i] = Integer.parseInt(strs[i]);
        }
        return intArr;
    }

    public static String getStr(byte[] bytes) {
        if (bytes != null) return new String(bytes);
        return null;
    }
}
