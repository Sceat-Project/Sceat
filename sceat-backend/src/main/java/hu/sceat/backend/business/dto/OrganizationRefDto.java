package hu.sceat.backend.business.dto;

import hu.sceat.backend.business.id.OrganizationId;

public record OrganizationRefDto(Long id, String name) implements OrganizationId {}
