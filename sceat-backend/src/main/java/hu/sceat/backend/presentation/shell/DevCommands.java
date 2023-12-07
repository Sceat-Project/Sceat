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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

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
		ThreadLocalRandom random = ThreadLocalRandom.current();
		
		OrganizationId org = orgService.create("Eötvös Középiskola").orElseThrow();
		
		UserId server = authService.registerServer("server-a@elte.hu", "password",
				"Merő Melinda", org.getId()).orElseThrow();
		authService.registerConsumer("consumer-a@elte.hu", "password",
				"Éhes Elek", org.getId());
		
		List<String> exampleMenuNames = List.of("Vegetáriánus menü", "Gluténmentes menü",
				"Vegán menü", "Laktózmentes menü", "Halmentes menü", "Tojásmentes menü",
				"Diabetikus menü", "Paleo menü", "Kóser menü", "Halal menü", "Szójamentes menü");
		List<String> exampleFoods = new ArrayList<>(List.of("Csirke", "Rizs", "Krumpli", "Torta", "Puding",
				"Pacal", "Káposzta", "Kolbász", "Sajt", "Kenyér", "Tészta", "Pizza", "Hamburger", "Hotdog",
				"Sültkrumpli", "Sültcsirke", "Rántottcsirke", "Rántottsajt", "Rántottkolbász", "Rántottkarfiol",
				"Rántottgomba", "Rántotttészta", "Rántottkrumpli", "Rántotttojás", "Rántottbrokkoli",
				"Rántottkaralábé", "Rántottzöldség", "Tojásleves", "Gombaleves", "Húsleves", "Zöldségleves",
				"Halászlé", "Bableves", "Krumplileves", "Palócleves", "Gulyásleves", "Káposztaleves", "Káposztásleves",
				"Káposztásbab", "Káposztásbabfőzelék", "Lasagne", "Spagetti Bolognese", "Spagetti Carbonara"));
		
		LocalDate startDate = LocalDate.now().minusWeeks(1);
		LocalDate endDate = LocalDate.now().plusWeeks(10);
		for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
			if (random.nextDouble() < 0.3) continue; //skip day with 30% chance
			
			for (Occasion occasion : Occasion.values()) {
				if (random.nextDouble() < 0.3) continue; //skip occasion with 30% chance
				
				Set<Allergen> allergens = new HashSet<>();
				if (random.nextDouble() < 0.8) { //no allergens with 20% chance
					for (Allergen allergen : Allergen.values()) {
						if (random.nextDouble() < 2.0 / Allergen.values().length) allergens.add(allergen);
					}
				}
				
				String name = exampleMenuNames.get(random.nextInt(exampleMenuNames.size()));
				int cost = 10 * random.nextInt(50, 300);
				Collections.shuffle(exampleFoods);
				List<String> foods = exampleFoods.stream()
						.limit(random.nextInt(0, 5))
						.toList();
				
				menuService.create(server, org.getId(), name, date, occasion, cost, foods, allergens);
			}
		}
		
		return "Database initialized with dummy data";
	}
}
