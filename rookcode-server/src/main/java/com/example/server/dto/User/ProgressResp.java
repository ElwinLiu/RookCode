package com.example.server.dto.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgressResp {
    int easy_solved;
    int easy_total;
    int medium_solved;
    int medium_total;
    int hard_solved;
    int hard_total;
}
