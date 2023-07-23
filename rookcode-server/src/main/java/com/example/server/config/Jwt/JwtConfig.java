package com.example.server.config.Jwt;

import com.example.server.util.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Jwt拦截器配置
 */
@Configuration
public class JwtConfig implements WebMvcConfigurer {
    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**") // 拦截所有请求
                // 不拦截swagger页面
                .excludePathPatterns("/swagger-resources/**")
                .excludePathPatterns("/doc.html/**")
                .excludePathPatterns("/webjars/**")
                // 不拦截登录请求
                .excludePathPatterns("/users/login")
                .excludePathPatterns("/captcha")
                .excludePathPatterns("/test/hello5")
                .excludePathPatterns("/test/updateAvatarById")
                .excludePathPatterns("/users/registerCode")
                .excludePathPatterns("/users/captcha")
                .excludePathPatterns("/users/register")
                .excludePathPatterns("/questions/getQuestions")
                .excludePathPatterns("/questions/getQuestion")
                .excludePathPatterns("/questions//getQuestionInfo")
                .excludePathPatterns("/users/getUserById")
                .excludePathPatterns("/solutions/findSolutions")
                .excludePathPatterns("/solutions/getSolutionsById")
                .excludePathPatterns("/solutions/getAllTags")
                .excludePathPatterns("/solutions/Getcomments")
                .excludePathPatterns("/users/publish/list")
                .excludePathPatterns("/hello5")
                .excludePathPatterns("/test/insertDaily")
                .excludePathPatterns("/user/progress")
                .excludePathPatterns("/user/year_submit")
                .excludePathPatterns("/user/solutionList")
                .excludePathPatterns("/user/pass/list")
                .excludePathPatterns("/users/getUserById")
                .excludePathPatterns("/user/discussionList")
                .excludePathPatterns("/test-case/PubTestCase")
                .excludePathPatterns("/test-case/UpdateTestCase")
                .excludePathPatterns("/test-case/DeleteTestCase/{id}")
                .excludePathPatterns("/questions/PublishQuestion")
                .excludePathPatterns("/questions/UpdateQuestion")
                .excludePathPatterns("/questions/DeleteQuestion/{id}")
                .excludePathPatterns("/records/GetList/{userId}/{questionId}")
                .excludePathPatterns("/records/GetRecordsDetail/{recordsId}")
                .excludePathPatterns("/discussions/getComments");

    }
}
