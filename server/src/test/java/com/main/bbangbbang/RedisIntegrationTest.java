package com.main.bbangbbang;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisServer;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RedisIntegrationTest {

    private RedisServer redisServer;
    @Test
    public void testRedis() {
//        redisServer = new RedisServer(6379);

    }
}
