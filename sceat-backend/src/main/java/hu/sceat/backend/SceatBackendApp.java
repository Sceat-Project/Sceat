package hu.sceat.backend;

import hu.sceat.backend.business.id.OrganizationId;
import hu.sceat.backend.business.id.UserId;
import hu.sceat.backend.business.service.AuthService;
import hu.sceat.backend.business.service.MenuService;
import hu.sceat.backend.business.service.OrganizationService;
import hu.sceat.backend.persistence.entity.Allergen;
import hu.sceat.backend.persistence.entity.Occasion;
import hu.sceat.backend.persistence.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class SceatBackendApp {
	private static final Logger logger = LoggerFactory.getLogger(SceatBackendApp.class);
	
	public static void main(String[] args) {
		SpringApplication.run(SceatBackendApp.class, args);
	}
	
	@Transactional
	@Bean
	public CommandLineRunner initDbWithExampleData(UserRepository userRepo, AuthService authService,
			OrganizationService orgService, MenuService menuService) {
		return args -> {
			if (Arrays.stream(args).noneMatch(x -> x.equalsIgnoreCase("--init-db-with-dummy-data")))
				return;
			
			// Check whether the database is empty
			if (userRepo.count() > 0) return;
			
			logger.info("Initializing database with dummy data...");
			
			OrganizationId org = orgService.create("SampleSchool").orElseThrow();
			
			UserId server = authService.registerServer("server-a", "password",
							"ServerA", org.getId()).orElseThrow();
			authService.registerConsumer("consumer-a", "password", "ConsumerA", org.getId());
			
			menuService.create(server, org.getId(), "Vegetarian Lunch", LocalDate.now(), Occasion.LUNCH, 42,
					List.of("Fish", "Chips"), Set.of(Allergen.FISH, Allergen.GLUTEN));
			
			logger.info("Database initialized with dummy data");
		};
	}
}
