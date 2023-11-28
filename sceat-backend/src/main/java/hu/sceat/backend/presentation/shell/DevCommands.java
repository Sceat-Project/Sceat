package hu.sceat.backend.presentation.shell;

import hu.sceat.backend.business.id.OrganizationId;
import hu.sceat.backend.business.id.UserId;
import hu.sceat.backend.business.service.AuthService;
import hu.sceat.backend.business.service.MenuService;
import hu.sceat.backend.business.service.OrganizationService;
import hu.sceat.backend.persistence.entity.Allergen;
import hu.sceat.backend.persistence.entity.Occasion;
import org.springframework.shell.command.annotation.Command;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Command(command = "dev")
public class DevCommands {
	private final AuthService authService;
	private final OrganizationService orgService;
	private final MenuService menuService;
	
	public DevCommands(AuthService authService, OrganizationService orgService, MenuService menuService) {
		this.authService = authService;
		this.orgService = orgService;
		this.menuService = menuService;
	}
	
	@Command(command = "initDbWithDummyValues")
	public String initDbWithDummyValues() {
		if (orgService.list().size() > 0) return "Database is not empty, aborting";
		
		OrganizationId org = orgService.create("SampleSchool").orElseThrow();
		
		UserId server = authService.registerServer("server-a@elte.hu", "password",
				"ServerA", org.getId()).orElseThrow();
		authService.registerConsumer("consumer-a@elte.hu", "password",
				"ConsumerA", org.getId());
		
		menuService.create(server, org.getId(), "Vegetarian Lunch", LocalDate.now(), Occasion.LUNCH, 42,
				List.of("Fish", "Chips"), Set.of(Allergen.FISH, Allergen.GLUTEN));
		
		return "Database initialized with dummy data";
	}
}
