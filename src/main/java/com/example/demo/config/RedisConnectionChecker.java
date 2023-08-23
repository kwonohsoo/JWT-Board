package com.example.demo.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisConnectionChecker {

    private final RedisConnectionFactory redisConnectionFactory;

    public void checkRedisConnection() {
        try {
            redisConnectionFactory.getConnection();
            log.info("Redis 연결 확인 : 성공");
        } catch (Exception e) {
            log.error("Redis 연결 확인 : 실패", e);
        }
    }
}
