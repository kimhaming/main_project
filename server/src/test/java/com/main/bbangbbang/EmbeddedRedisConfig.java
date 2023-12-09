//package com.main.bbangbbang;
//
//import com.main.bbangbbang.exception.BusinessLogicException;
//import lombok.Value;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.data.redis.connection.RedisServer;
//import org.springframework.util.StringUtils;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//
//@Slf4j
//@Profile("local")
//@Configuration
//public class EmbeddedRedisConfig {
//
//    @Value("${spring.redis.port}")
//    private int redisPort;
//
//    private RedisServer redisServer;
//
//    @PostConstruct
//    public void startRedis() throws IOException {
//        int port = isRedisRunning() ? findAvailablePort() : redisPort;
//        redisServer = new RedisServer(port);
//        redisServer.start();
//    }
//
//    @PreDestroy
//    public void stopRedis() {
//        redisServer.stop();
//    }
//
//    public int findAvailablePort() throws IOException {
//        for (int port = 10000; port <= 65535; port++) {
//            Process process = executeGrepProcessCommand(port);
//            if (!isRunning(process)) {
//                return port;
//            }
//        }
//
//        throw new BusinessLogicException(ExceptionCode.NOT_FOUND_AVAILABLE_PORT);
//    }
//
//    /**
//     * Embedded Redis가 현재 실행중인지 확인
//     */
//    private boolean isRedisRunning() throws IOException {
//        return isRunning(executeGrepProcessCommand(redisPort));
//    }
//
//    /**
//     * 해당 Process가 현재 실행중인지 확인
//     */
//    private boolean isRunning(Process process) {
//        String line;
//        StringBuilder pidInfo = new StringBuilder();
//
//        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//            while ((line = input.readLine()) != null) {
//                pidInfo.append(line);
//            }
//        } catch (Exception e) {
//            throw new BusinessLogicException(ExceptionCode.ERROR_EXECUTING_EMBEDDED_REDIS);
//        }
//        return StringUtils.hasText(pidInfo.toString());
//    }
//}
