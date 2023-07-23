package com.example.server.dto.Record;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassInfoListResp {
    int total_page;
    List<PassInfoResp> infoRespList;
}
