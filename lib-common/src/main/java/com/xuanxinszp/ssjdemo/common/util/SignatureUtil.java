package com.xuanxinszp.ssjdemo.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by Benson on 2018/4/2.
 */
public class SignatureUtil {


    private static Logger logger= LoggerFactory.getLogger(SignatureUtil.class);

    public static final String FIELD_SIGN = "sign";
    /**
     * 校验签名是否正确
     * 必须包含sign字段，否则返回false。
     *
     * @param data Map类型数据
     * @param key API密钥
     * @return 签名是否正确
     * @throws Exception
     */
    public static boolean isValid(Map<String, String> data, String key) throws Exception {
        if (null==data || data.isEmpty()) {
            return false;
        }
        if (!data.containsKey(FIELD_SIGN)) {
            return false;
        }
        String sign = data.get(FIELD_SIGN);
        String sysSign=generateSignature(data, key);
        logger.info("\n ##系统签名:{}，接口签名：{}",sysSign,sign);
        return sysSign.equals(sign);
    }


    /**
     * 校验签名是否正确
     * 必须包含sign字段，否则返回false。
     *
     * @param data Map类型数据
     * @param key API密钥
     * @return 签名是否正确
     * @throws Exception
     */
    public static <T> boolean isValid(T data, String key) throws Exception {
        if (null == data) {
            return false;
        }
        Map<String,String> params = ConvertUtil.convertBeanToMap(data);
        return isValid(params,key);
    }


    /**
     * 生成签名
     *
     * @param data 待签名数据
     * @param key API密钥
     * @return 签名
     */
    public static String generateSignature(final Map<String, String> data, String key) throws Exception {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            // 过滤签名字段
            if (FIELD_SIGN.equals(k)) {
                continue;
            }
            String v = data.get(k);
            if (null!=v && v.trim().length() > 0) // 若参数值为空，则不参与签名
                sb.append(k).append("=").append(v.trim()).append("&");
        }
        sb.append("key=").append(key);
        logger.info("\n ## 需要签名的字符串：{}",sb.toString());
        return EncryptionUtil.md5(sb.toString()).toUpperCase();
    }

    public static <T> String generateSignature(T data,String key) throws Exception {
        Map<String,String> map = ConvertUtil.convertBeanToMap(data);
        return generateSignature(map,key);
    }

    /**
     * 获取随机字符串 Nonce Str
     *
     * @return String 随机字符串
     */
    public static String generateNonceStr() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }
}
