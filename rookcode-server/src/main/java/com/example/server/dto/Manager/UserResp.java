package com.example.server.dto.Manager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResp {
    private String avatar;
    private String account;
    private String nickname;
    private String description;
    private int id;
}
