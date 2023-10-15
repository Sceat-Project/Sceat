package hu.sceat.backend.util;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestConfiguration
public class SpringTestConfig {
	
	// bcrypt is very slow, so replace it with a faster alternative
	@Bean
	@Primary
	public PasswordEncoder fastPasswordEncoderForTesting() {
		//noinspection deprecation
		return new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-256");
	}
}
