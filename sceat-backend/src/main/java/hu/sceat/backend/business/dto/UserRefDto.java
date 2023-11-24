package hu.sceat.backend.business.dto;

import hu.sceat.backend.business.id.UserId;

public record UserRefDto(Long id, String email, String name) implements UserId {}
