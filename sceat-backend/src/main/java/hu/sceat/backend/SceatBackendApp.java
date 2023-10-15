package hu.sceat.backend;

import hu.sceat.backend.persistence.entity.User;
import hu.sceat.backend.persistence.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@SpringBootApplication
public class SceatBackendApp {
	private static final Logger logger = LoggerFactory.getLogger(SceatBackendApp.class);
	
	public static void main(String[] args) {
		SpringApplication.run(SceatBackendApp.class, args);
	}
	
	@Transactional
	@Bean
	public CommandLineRunner initDbWithExampleData(PasswordEncoder passwordEncoder, UserRepository userRepo) {
		return args -> {
			if (Arrays.stream(args).noneMatch(x -> x.equalsIgnoreCase("--init-db-with-dummy-data")))
				return;
			
			// Check whether the database is empty
			if (userRepo.count() > 0) return;
			
			logger.info("Initializing database with dummy data...");
			
			userRepo.save(User.create("user", passwordEncoder.encode("password")));
			
			logger.info("Database initialized with dummy data");
		};
	}
}
