package com.main.bbangbbang.auth.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@RequiredArgsConstructor    // final 필드를 자동으로 생성해주는 어노테이션
@Configuration  // 설정 파일임을 알려주는 어노테이션
@EnableRedisRepositories    // RedisRepository 사용을 위한 어노테이션
public class RedisRepositoryConfig {
    private final RedisProperties redisProperties;

    // RedisConnectionFactory를 빈으로 등록
    // RedisConnectionFactory는 Redis 서버에 연결하는 인터페이스
    // LettuceConnectionFactory는 RedisConnectionFactory의 구현체 중 하나
    // RedisProperties로 yml에 저장한 host, post를 가지고 와서 연결한다.
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
    }

    // RedisTemplate을 빈으로 등록
    // RedisTemplate은 Redis 서버에 연결하여 데이터를 주고 받는 인터페이스
    // setKeySerializer, setValueSerializer 설정으로 redis-cli를 통해 직접 데이터를 확인할 수 있도록 한다.
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }
}
