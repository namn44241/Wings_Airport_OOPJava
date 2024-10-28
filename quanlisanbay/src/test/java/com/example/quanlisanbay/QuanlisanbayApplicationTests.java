package com.example.quanlisanbay;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import javax.sql.DataSource;

@SpringBootTest
class QuanlisanbayApplicationTests {

	@Test
	void contextLoads() {
		// Đây là kiểm thử cơ bản để tải context
	}

	@TestConfiguration
	static class TestConfig {

		@Bean
		@Primary
		public DataSource dataSource() {
			return new EmbeddedDatabaseBuilder()
					.setType(EmbeddedDatabaseType.H2)
					.build();
		}
	}
}