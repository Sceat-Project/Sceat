package hu.sceat.backend.business.dto;

import hu.sceat.backend.persistence.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DtoMapper {
	DtoMapper INSTANCE = Mappers.getMapper(DtoMapper.class);
	
	UserDto toUser(User user);
}
