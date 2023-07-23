package com.example.server.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Jwt工具类，用于生成token和验证token
 */
public class JwtUtils {
    private static final String SECRET_KEY = "cugSE2023";
    private static final long EXPIRATION_TIME = 3600_000; // 1 hour
    private static final String HEADER_AUTHORIZATION = "Authorization";

    public static String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", username);
        claims.put("created", new Date());
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME * 10000))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public static boolean validateToken(String token, String username) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            String subject = claims.getSubject();
            Date expiration = claims.getExpiration();
            Date now = new Date();
            return subject.equals(username) && now.before(expiration);
        } catch (Exception ex) {
            return false;
        }
    }

    public static String getSecretKey() {
        return SECRET_KEY;
    }

    // 从http请求中解析username
    public static String getUsernameFromRequest(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(HEADER_AUTHORIZATION);
        return Jwts.parser()
                .setSigningKey(JwtUtils.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}