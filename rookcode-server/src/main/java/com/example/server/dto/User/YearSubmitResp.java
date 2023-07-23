package com.example.server.dto.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YearSubmitResp {
    // 日期格式为yyyy-MM-dd，如2023-01-01
    LocalDate date;
    // 该日的提交数量
    int submit;
}
