package com.example.server.dto.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUserByTokenResp {
    private String nickname;
    private String account;
    private String avatar;
    private String description;
}
