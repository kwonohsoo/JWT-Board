package com.example.demo;

import com.example.demo.config.RedisConnectionChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.PostConstruct;

@EnableJpaAuditing
@SpringBootApplication
@RequiredArgsConstructor
public class DemoApplication {

	private final RedisConnectionChecker redisConnectionChecker;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	// Redis 실행 체크
	@PostConstruct
	public void checkRedisOnStartUp() {
		redisConnectionChecker.checkRedisConnection();
	}
}
