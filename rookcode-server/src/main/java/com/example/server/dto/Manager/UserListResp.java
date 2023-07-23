package com.example.server.dto.Manager;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListResp {
    List<UserResp> userRespList;
    int total_cnt;
}
