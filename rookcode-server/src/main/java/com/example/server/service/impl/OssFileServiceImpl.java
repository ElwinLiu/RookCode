package com.example.server.service.impl;

import com.example.server.service.IOssFileService;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.DownloadUrl;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;


/**
 * @author YFMan
 * @Description 七牛云oss存储
 * @Date 2023/4/21 21:28
 */

@Service
public class OssFileServiceImpl implements IOssFileService {
    // 七牛云的相关配置，通过 yml 映射到变量中
    @Value("${qiniu.kodo.ACCESS_KEY}")
    String accessKey; // AK
    @Value("${qiniu.kodo.SECRET_KEY}")
    String secretKey; // SK
    @Value("${qiniu.kodo.BUCKET_NAME}")
    String bucket; // 七牛云上的空间名称
    @Value("${qiniu.kodo.DOMAIN}")
    String domain; // 七牛云空间对应的域名

    /*
     * @Author YFMan
     * @Description 根据文件的 hashKey，获取其对应的url
     * @Date 2023/4/22 11:13
     * @Param [fileKey] 文件的 hashKey
     * @return java.lang.String 文件的对象存储 url
     **/
    String getUrlByKey(String fileKey) throws QiniuException {
        // 根据域名、协议、fileKey 拼接生成 url
        DownloadUrl url = new DownloadUrl(domain, false, fileKey);
        // 构建 url 字符串
        String urlString = url.buildURL();

        System.out.println(urlString);

        return urlString;
    }

    /*
     * @Author YFMan
     * @Description 将字符串上传为 oss 的文件，并获得对应的 url
     * @Date 2023/4/22 11:19
     * @Param [str] 要存储的字符串
     * @return java.lang.String 文件的 url
     **/
    @Override
    public String uploadFile(String str) {
        // 构造一个带指定Region对象的配置类
        Configuration cfg = new Configuration(Region.region2()); // Region.region2() 表示华南区域
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本

        UploadManager uploadManager = new UploadManager(cfg);

        // 转化为字节数组
        byte[] uploadBytes = str.getBytes(StandardCharsets.UTF_8);
        // 转化为数据流
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(uploadBytes);
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        String fileUrl = "";
        try {
            // 默认不指定key的情况下，以文件内容的hash值作为文件名
            Response response = uploadManager.put(byteInputStream, null, upToken, null, null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
            fileUrl = getUrlByKey(putRet.key);
            System.out.println(fileUrl);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }

        return fileUrl;
    }
    /*
     * @Author YFMan
     * @Description 根据 url 下载对应的文件，并转换为string类型
     * @Date 2023/4/28 16:21
     * @Param [url] url 对应的测试用例
     * @return java.lang.String
     **/
    @Override
    public String downloadFile(String url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");

        // 获取返回值
        int status = httpURLConnection.getResponseCode();
        // 如果连接异常，返回 null
        if(status != 200){
            return null;
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        httpURLConnection.disconnect();

        // 将返回的内容转换为字符串
        String resourceString = content.toString();
        System.out.println("Resource Content: " + resourceString);
        return resourceString;
    }
}
