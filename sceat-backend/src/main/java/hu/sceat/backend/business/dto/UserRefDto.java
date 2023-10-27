package hu.sceat.backend.business.dto;

import hu.sceat.backend.business.id.UserId;

public record UserRefDto(Long id, String username) implements UserId {}
