package com.example.server.dto.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest implements Serializable {
    // 邮件接收方
    private String[] tos;
    // 邮件主题
    private String subject;
    // 邮件内容
    private String content;
}
