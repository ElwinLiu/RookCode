package com.example.server.util;
import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jwt拦截器，校验请求头token
 */
@Component
@Order(1) // 设置较低的优先级,防止先于swagger进行拦截
public class JwtInterceptor implements HandlerInterceptor {
    private static final String HEADER_AUTHORIZATION = "Authorization";
/*    private static final String TOKEN_PREFIX = "Bearer ";*/

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(HEADER_AUTHORIZATION);
        System.out.println(token);
        if (token != null) {
            String username = getUsernameFromToken(token);
            if (JwtUtils.validateToken(token, username)) {
                request.setAttribute("username", username);
                return true;
            }
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }

    private String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(JwtUtils.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
