package com.xuanxinszp.ssjdemo.common.bean.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.xuanxinszp.ssjdemo.common.dto.OSSPolicyDto;
import com.xuanxinszp.ssjdemo.common.enums.OssDirType;
import com.xuanxinszp.ssjdemo.common.util.FileDownloadUtil;
import com.xuanxinszp.ssjdemo.common.util.TimeUtil;
import com.xuanxinszp.ssjdemo.common.web.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *  阿里云OSS相关接口封装
 * Created by Benson on 2018/3/28.
 */
@Component
public class OSSManager {

    private static Logger log = LoggerFactory.getLogger(OSSManager.class);

    @Autowired
    private OSSConfig config;

    public OSSPolicyDto getPolicy() {

        String host = "http://" + config.getBucket() + "." + config.getEndpoint();
        Map<String, String> respMap = new LinkedHashMap<String, String>();
        OSSPolicyDto dto = new OSSPolicyDto();
        try {
            String dir = config.getDir();

            long expireEndTime = System.currentTimeMillis() + config.getPolicyExpireTime() * 1000;
            OSSClient client = new OSSClient(config.getEndpoint(), config.getAccessId(), config.getAccessKey());
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = client.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = client.calculatePostSignature(postPolicy);

            dto.setAccessId(config.getAccessId());
            dto.setPolicy(encodedPolicy);
            dto.setSignature(postSignature);
            dto.setDir(dir);
            dto.setHost(host);
            dto.setExpire(String.valueOf(expireEndTime / 1000));

        } catch (UnsupportedEncodingException e) {
            log.error("OSS获取policy发生异常：" + e);
        }
        return dto;
    }


    /**
     * 上传附件
     * @param ossDirType OSS目录类型
     * @param containDateDir 是否包含日期目录
     * @param inputStream 文件输入流程
     * @param fileName 文件名称
     * @return 上传后的地址
     */
    public String upload(OssDirType ossDirType, Boolean containDateDir, InputStream inputStream, String fileName){
        OSSClient client = new OSSClient(config.getEndpoint(), config.getAccessId(), config.getAccessKey());
        String dirStr = config.getDir() + ossDirType.getDir() + "/";
        if (containDateDir) {
            dirStr = dirStr + TimeUtil.getDateSerialStr() + "/";
        }
        log.info("\n###上传路径：{}，上传文件：{}", dirStr, fileName);
        client.putObject(config.getBucket(), dirStr + fileName, inputStream);

        String afterUploadUrl = getOssDir(ossDirType, containDateDir) + fileName;
        log.info("\n###上传后的地址：{}", afterUploadUrl);

        return afterUploadUrl;
    }

    /**
     * 获取OSS目录
     * @param ossDirType OSS目录类型
     * @param containDateDir 是否包含日期目录
     * @return
     */
    public String getOssDir(OssDirType ossDirType, Boolean containDateDir) {
        String host = "http://" + config.getBucket() + "." + config.getEndpoint();
        String dirStr = config.getDir() + ossDirType.getDir() + "/";
        if (containDateDir) {
            dirStr = dirStr + TimeUtil.getDateSerialStr() + "/";
        }
        return host + "/" + dirStr;
    }


    /**
     * 从网络下载文件并上传至OSS
     * @param urlStr 网络地址
     * @param fileName 文件名，包含后缀
     * @param ossDirType OSS目录类型
     * @param containDateDir 是否包含日期目录
     * @return 上传后的地址
     */
    public String uploadFromUrl(String urlStr, String fileName, OssDirType ossDirType, Boolean containDateDir){
        InputStream inputStream = null;
        String afterUploadUrl = null;
        try {
            inputStream = FileDownloadUtil.downloadFromUrl(urlStr);
            afterUploadUrl = upload(ossDirType, containDateDir, inputStream, fileName);
        } catch (IOException e) {
            log.error("从网络下载文件并上传至OSS时发生异常：", e);
            throw new AppException("从网络下载文件并上传至OSS时发生异常");
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("关闭输入流时发生异常：", e);
                }
            }
        }

        return afterUploadUrl;
    }
}
