package hu.sceat.backend.business.dto;

import hu.sceat.backend.business.id.UserId;

import java.util.Optional;

public record UserDto(Long id, String username, Optional<ServerDto> serverProfile,
		Optional<ConsumerDto> consumerProfile) implements UserId {}
