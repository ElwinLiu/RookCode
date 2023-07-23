package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.server.dto.Resp;
import com.example.server.mapper.LanguagesMapper;
import com.example.server.pojo.Records;
import com.example.server.vo.RecordsDetailVO;
import com.example.server.vo.RecordsVO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Elwin
 * @since 2023-04-12
 */
public interface IRecordsService extends IService<Records> {
    Resp<List<RecordsVO>> getRecordsList(String account, Integer questionId);

    // 获取每条提交记录的详细信息
    Resp<RecordsDetailVO> getRecordsDetail(Integer recordsId);

    List<RecordsVO> convertRecordsToRecordsVO(List<Records> recordsList);

}
