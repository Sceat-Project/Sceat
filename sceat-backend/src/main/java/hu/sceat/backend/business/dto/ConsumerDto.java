package hu.sceat.backend.business.dto;

import hu.sceat.backend.persistence.entity.Allergen;

import java.util.Set;

public record ConsumerDto(UserRefDto user, OrganizationRefDto organization, Set<Allergen> allergies) {
	public ConsumerDto {
		allergies = Set.copyOf(allergies);
	}
}
