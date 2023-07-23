package com.example.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.mapper.LanguagesMapper;
import com.example.server.pojo.Languages;
import com.example.server.service.ILanguagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Elwin
 * @since 2023-04-12
 */
@Service
public class LanguagesServiceImpl extends ServiceImpl<LanguagesMapper, Languages> implements ILanguagesService {

}
