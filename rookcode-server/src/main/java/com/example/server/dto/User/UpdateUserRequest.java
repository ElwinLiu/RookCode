package com.example.server.dto.User;

import lombok.Data;

@Data
public class UpdateUserRequest {
    String nickname;
    String description;
    String avatar;
}
