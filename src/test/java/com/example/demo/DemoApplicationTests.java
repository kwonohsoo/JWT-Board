package com.example.demo;

import com.example.demo.config.RedisConnectionChecker;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.PostConstruct;

@SpringBootTest
@RequiredArgsConstructor
class DemoApplicationTests {

	private final RedisConnectionChecker redisConnectionChecker;

	@Test
	void contextLoads() {
	}

	@PostConstruct
	public void checkRedisOnStartUpTest() {
		redisConnectionChecker.checkRedisConnection();
	}

}
