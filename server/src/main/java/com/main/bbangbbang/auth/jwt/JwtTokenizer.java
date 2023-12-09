package com.main.bbangbbang.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenizer {
    private final RedisTemplate<String, Long> redisTemplate;

    @Getter
    @Value("${jwt.key.secret}")
    private String secretKey;

    @Getter
    @Value("${jwt.access-token-expiration-minutes}")
    private int accessTokenExpirationMinutes;

    @Getter
    @Value("${jwt.refresh-token-expiration-minutes}")
    private int refreshTokenExpirationMinutes;

    @Getter
    @Value("${jwt.fixed-expiration-minutes}")
    private int fixedExpirationMinutes;

//    @Getter
//    private final Map<String, Long> tokenBlackList = new HashMap<>();
    public JwtTokenizer(RedisTemplate<String, Long> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String encodeBase64SecretKey(String secretKey) {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Map<String, Object> claims,
                                      String subject,
                                      Date expiration,
                                      String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(String subject, Date expiration, String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    // 검증 후, Claims을 반환하는 용도
    public Jws<Claims> getClaims(String jws, String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

//        // Redis에서 토큰이 블랙리스트에 있는지 확인
        if (redisTemplate.opsForValue().get(jws) != null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "블랙리스트에 있는 토큰입니다.");
        }

        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jws);
        return claims;
    }

    // 단순히 검증만 하는 용도로 쓰일 경우
    public Jws<Claims> verifySignature(String jws) {
        // 주어진 jwt 토큰이 블랙리스트에 있는지 확인하여 만료 처리된 토큰인지 확인
        if (redisTemplate.opsForValue().get(jws) != null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다.");
        }
        try {   // parser로 토큰이 올바르게 서명되었는지 확인하고 Claims를 반환
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(jws);
        } catch (ExpiredJwtException exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다.");
        }
    }

    public Date getTokenExpiration(int expirationMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expirationMinutes);
        Date expiration = calendar.getTime();

        return expiration;
    }

    private Key getKeyFromBase64EncodedKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return key;
    }

    // 토큰을 Redis에 저장하는 핵심 메서드
    public void addToRedisTokenBlacklist(String jws) {
        // fixedExpirationMinutes 를 사용하여 토큰의 만료 시간을 설정하는 코드 추가
        long expirationTime = System.currentTimeMillis() + (fixedExpirationMinutes * 60 * 1000); // 현재 시간 + 만료 시간(ms)
        redisTemplate.opsForValue().set(jws, expirationTime);   // 토큰을 Redis에 저장하며, 현재 시간을 value로 저장
    }
    // System.currentTimeMillis() 는 현재 시간을 밀리초로 반환한 것이기 때문에 fixedExpirationMinutes를 밀리초로 변환하여
    // 현재 시간에 더해주면 토큰의 만료 시간을 설정할 수 있습니다.
    // 이 방법을 사용하면 현재 시간을 기준으로 얼마나 뒤에 만료되는지 확인할 수 있습니다.

    public boolean isTokenBlacklisted(String jws) {
        return redisTemplate.opsForValue().get(jws) != null;
    }

    private Key getKey() {
        String base64EncodedSecretKey = encodeBase64SecretKey(getSecretKey());

        return getKeyFromBase64EncodedKey(base64EncodedSecretKey);
    }
}
