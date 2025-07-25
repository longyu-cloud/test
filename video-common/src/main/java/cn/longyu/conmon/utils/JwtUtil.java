package cn.longyu.conmon.utils;


import cn.longyu.conmon.exception.VideoException;
import cn.longyu.conmon.result.ResultCodeEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {

    private static long tokenExpiration = 60 * 60 * 1000L;
    private static SecretKey tokenSignKey = Keys.hmacShaKeyFor("M0PKKI6pYGVWWfDZw90a0lTpGYX1d4AQ".getBytes());

    public static String createToken(Long userId, String username) {
        String token = Jwts.builder().
                setSubject("LOGIN_INFO").
                setExpiration(new Date(System.currentTimeMillis() + tokenExpiration)).
//                自己的属性声明
                claim("userId", userId).
                claim("username", username).
                signWith(tokenSignKey, SignatureAlgorithm.HS256).
                compact();
        return token;
    }
    public static Claims parseToke(String token){
        if (token==null){
            throw new VideoException(ResultCodeEnum.APP_LOGIN_AUTH);
        }
        try {
            // 获取载荷
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(tokenSignKey)  // 校验秘钥
                    .build()                 // 构建解析器
                    .parseClaimsJws(token) // 解析并校验签名
                    .getBody();
            return claims;

        } catch (ExpiredJwtException e) {
            // token 过期
            throw new VideoException(ResultCodeEnum.TOKEN_EXPIRED);
        } catch (JwtException e) {
            // 签名错误、格式错误等
            throw new VideoException(ResultCodeEnum.ADMIN_LOGIN_AUTH);
        }

    }

}