package com.example.server.dto.User;

import lombok.Data;

@Data
public class ChangeNameRequest {
    private int id;
    private String nickname;
}
