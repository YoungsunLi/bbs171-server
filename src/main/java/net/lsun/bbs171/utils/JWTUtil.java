package net.lsun.bbs171.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.Map;

public class JWTUtil {
    // 前缀
    public static final String TOKEN_PREFIX = "Bearer ";
    // 表头授权
    public static final String AUTHORIZATION = "Authorization";
    // 有效时间
    private static final int EXPIRATION_TIME = 24 * 60 * 60 * 1000;
    // 秘钥
    private static final String SECRET = "AQye22dcXV1jCrzzC4iGbyM68LADwPSs11=AQye22dcXV1jCrzzC4iGbyM68LADwPSs11";

    /**
     * 创建 token
     *
     * @param id userID
     * @return token
     */
    public static String generateToken(int id) {
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));
        String token = Jwts.builder()
                .claim("user", id)
                .setIssuer("bbs171.lsun.net")
                //签发时间
                .setIssuedAt(new Date())
                //过期时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TOKEN_PREFIX + token;
    }

    /**
     * 校验 token
     *
     * @param token token
     * @return phone
     */
    public static String validateToken(String token) {
        Map<String, Object> body = Jwts.parserBuilder()
                .setSigningKey(SECRET)
                .build()
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody();

        return body.get("user").toString();
    }
}
