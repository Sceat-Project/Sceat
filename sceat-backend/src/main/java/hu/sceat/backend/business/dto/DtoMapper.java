package hu.sceat.backend.business.dto;

import hu.sceat.backend.persistence.entity.Consumer;
import hu.sceat.backend.persistence.entity.Menu;
import hu.sceat.backend.persistence.entity.Organization;
import hu.sceat.backend.persistence.entity.Server;
import hu.sceat.backend.persistence.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@Mapper
public interface DtoMapper {
	DtoMapper INSTANCE = Mappers.getMapper(DtoMapper.class);
	
	OrganizationDto toOrganization(Organization organization);
	UserDto toUser(User user);
	UserRefDto toUserRef(User user);
	ServerDto toServer(Server server);
	ConsumerDto toConsumer(Consumer consumer);
	MenuDto toMenu(Menu menu);
	
	default Optional<ServerDto> mapOptionalServer(Optional<Server> server) {
		return server.map(this::toServer);
	}
	default Optional<ConsumerDto> mapOptionalConsumer(Optional<Consumer> consumer) {
		return consumer.map(this::toConsumer);
	}
}
