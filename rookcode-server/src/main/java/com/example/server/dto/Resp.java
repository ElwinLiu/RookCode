package com.example.server.dto;

import lombok.Data;

@Data
public class Resp<T> {
    private Boolean success;
    private String message;
    private T data;

    public static <T> Resp<T> success(T data) {
        Resp<T> response = new Resp<>();
        response.success = true;
        response.setMessage("请求成功");
        response.setData(data);
        return response;
    }
    public static <T> Resp<T> fail(String message) {
        Resp<T> response = new Resp<>();
        response.success = false;
        response.setMessage(message);
        response.setData(null);
        return response;
    }


}
