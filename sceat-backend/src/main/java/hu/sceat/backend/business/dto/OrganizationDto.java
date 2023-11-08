package hu.sceat.backend.business.dto;

import hu.sceat.backend.business.id.OrganizationId;

public record OrganizationDto(Long id, String name) implements OrganizationId {}
