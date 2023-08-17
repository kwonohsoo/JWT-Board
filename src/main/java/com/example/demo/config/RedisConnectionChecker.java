package com.example.demo.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisConnectionChecker {

    private final RedisConnectionFactory redisConnectionFactory;

    private final Logger logger = LoggerFactory.getLogger(RedisConnectionChecker.class);

    public void checkRedisConnection() {
        try {
            redisConnectionFactory.getConnection();
            logger.info("Redis 연결 확인 : 성공");
        } catch (Exception e) {
            logger.error("Redis 연결 확인 : 실패", e);
        }
    }
}
