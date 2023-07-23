package com.example.server.service;

import lombok.Value;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * @author YFMan
 * @Description 对象存储服务接口
 * @Date 2023/4/21 20:51
 */
public interface IOssFileService {
    String uploadFile(String str);

    String downloadFile(String url) throws IOException;
}
