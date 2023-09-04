package com.example.demo;

import com.example.demo.config.RedisConnectionChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private RedisConnectionChecker redisConnectionChecker;

    @BeforeEach
    public void checkRedisOnStartUpTest() {
        redisConnectionChecker.checkRedisConnection();
    }

    @Test
    void contextLoads() {
    }
}
//	given(준비): 어떠한 데이터가 준비되었을 때

//	when(실행): 어떠한 함수를 실행하면

//	then(검증): 어떠한 결과가 나와야 한다.