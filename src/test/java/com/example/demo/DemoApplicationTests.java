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