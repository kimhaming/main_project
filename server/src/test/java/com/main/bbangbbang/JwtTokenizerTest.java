//package com.main.bbangbbang;
//
//import com.main.bbangbbang.auth.jwt.JwtTokenizer;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.connection.RedisServer;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//public class JwtTokenizerTest {
//
//    @Autowired  // @SpringBootTest 애너테이션이 있어야 사용 가능
//    private JwtTokenizer jwtTokenizer;
//
//    @BeforeAll
//    public static void startRedisServer() {
//        // Embedded Redis 서버 시작
////        RedisServer.builder().port(6379).build().start();
//    }
//
//    @DisplayName("토큰 블랙리스트에 토큰 추가") // 테스트 이름 표시
//    @Test
//    public void testTokenBlacklist() {
//
//        String jwt = "your_sample_jwt_here";
//        jwtTokenizer.addToRedisTokenBlacklist(jwt);
//
//        // assertEquals() 메서드 -> 첫 번째 인자와 두 번째 인자가 같은지 검증
//        assertEquals(true, jwtTokenizer.isTokenBlacklisted(jwt));
//    }
//
////    @Test
////    public void test2() {
////
////    }
////
////    @Test
////    public void test3() {
////
////    }
////
////    @Test
////    public void test4() {
////
////    }
//
//
//}
