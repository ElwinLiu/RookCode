package com.example.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.server.pojo.Languages;
import com.example.server.pojo.Users;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Elwin
 * @since 2023-04-12
 */
public interface LanguagesMapper extends BaseMapper<Languages> {
    String selectLanguageNameById(Integer languageId);
}
