package com.main.bbangbbang.auth.controller;

import com.main.bbangbbang.auth.jwt.JwtTokenizer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenizer jwtTokenizer;

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Authorization header is not valid");
        }

        String[] parts = authorizationHeader.split(" ");
        String jws;

        if (parts.length > 1) {
            jws = parts[1];
        } else {
            return ResponseEntity.badRequest().body("Invalid Authorization header format.");
        }

        // JwtTokenizer의 addToRedisTokenBlacklist 메서드 호출
        jwtTokenizer.addToRedisTokenBlacklist(jws);

        return ResponseEntity.ok().body("Successfully logged out");
    }
}
