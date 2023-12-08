package hu.sceat.backend.business.service;

import hu.sceat.backend.business.dto.DtoMapper;
import hu.sceat.backend.business.dto.ServerDto;
import hu.sceat.backend.business.fail.CommonFail;
import hu.sceat.backend.business.fail.Fail;
import hu.sceat.backend.business.id.UserId;
import hu.sceat.backend.persistence.entity.Server;
import hu.sceat.backend.persistence.entity.User;
import hu.sceat.backend.util.Try;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ServerService {
	
	private final UserService userService;
	
	public ServerService(UserService userService) {
		this.userService = userService;
	}
	
	@Transactional
	public Try<ServerDto, Fail> findById(UserId requester, Long userId) {
		return get(requester, userId)
				.map(DtoMapper.INSTANCE::toServer);
	}
	
	private Try<Server, Fail> get(UserId requester, Long userId) {
		return userService.getById(requester, userId)
				.filter(User::isServer, CommonFail.invalidAction("not a server"))
				.map(u -> u.getServerProfile().orElseThrow());
	}
}
