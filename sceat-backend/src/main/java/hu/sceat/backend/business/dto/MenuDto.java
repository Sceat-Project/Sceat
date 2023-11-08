package hu.sceat.backend.business.dto;

import hu.sceat.backend.business.id.MenuId;
import hu.sceat.backend.persistence.entity.Allergen;
import hu.sceat.backend.persistence.entity.Occasion;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record MenuDto(Long id, OrganizationRefDto organization, String name, LocalDate date, Occasion occasion,
		int cost, List<String> foods, Set<Allergen> allergens) implements MenuId {
	public MenuDto {
		foods = List.copyOf(foods);
		allergens = Set.copyOf(allergens);
	}
}
