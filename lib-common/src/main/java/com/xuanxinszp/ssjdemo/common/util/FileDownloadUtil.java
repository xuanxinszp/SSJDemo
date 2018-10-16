package com.xuanxinszp.ssjdemo.common.util;

import com.google.common.collect.Lists;
import org.apache.commons.io.input.BOMInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * 文件下载工具类
 * Created by Benson on 2018/6/7.
 */
public class FileDownloadUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileDownloadUtil.class);

    /**
     * 从网络Url中下载文件
     * @param urlStr 网络地址
     * @param fileName 文件名，包含后缀
     * @param savePath 保存地址
     * @throws IOException
     */
    public static void  downloadFromUrl(String urlStr,String fileName,String savePath) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);

        //文件保存位置
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
        File file = new File(saveDir + File.separator + fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if (fos != null) {
            fos.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }

        logger.info("tips: {} download success.", url);
    }

    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }


    /**
     * 从网络Url中下载文件，并返回流
     * @param urlStr
     * @return
     * @throws IOException
     */
    public static InputStream  downloadFromUrl(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        //得到输入流
        InputStream inputStream = conn.getInputStream();
        return inputStream;
    }


    /**
     * 从网络Url中读取文件内容，支持超大文件读取，
     * 但是，此方法仅适合按行读取的文件格式。
     * @param url
     * @return
     * @throws IOException
     */
    public static List<String> readFileFromUrl(String url) {
        List<String> contents = Lists.newArrayList();

        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = downloadFromUrl(url);
            //reader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"),5*1024*1024);// 用5M的缓冲读取文本文件
            //使用BOMInputStream自动去除UTF-8中的BOM
            reader = new BufferedReader(new InputStreamReader(new BOMInputStream(inputStream),"utf-8"),5*1024*1024);// 用5M的缓冲读取文本文件

            String line = null;
            while((line = reader.readLine()) != null){
                contents.add(line.trim());
            }
        } catch (Exception e) {
            logger.error("从网络Url中读取文件内容异常", e);
        } finally {
            // 释放文件流
            if (null!=inputStream) try {inputStream.close();} catch (IOException e) {logger.error("释放文件流inputStream异常", e);}
            if (null!=reader) try {reader.close();} catch (IOException e) {logger.error("释放缓冲流BufferedReader异常", e);}
        }

        return contents;
    }

}
