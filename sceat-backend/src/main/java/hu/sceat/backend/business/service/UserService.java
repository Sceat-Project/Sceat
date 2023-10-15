package hu.sceat.backend.business.service;

import hu.sceat.backend.business.dto.DtoMapper;
import hu.sceat.backend.business.dto.UserDto;
import hu.sceat.backend.business.fail.CommonFail;
import hu.sceat.backend.business.fail.Fail;
import hu.sceat.backend.business.id.UserId;
import hu.sceat.backend.persistence.entity.User;
import hu.sceat.backend.persistence.repository.UserRepository;
import hu.sceat.backend.util.Try;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UserService { //TODO this is just an example service, we might not need it
	
	private final UserRepository userRepo;
	
	public UserService(UserRepository userRepo) {
		this.userRepo = userRepo;
	}
	
	@Transactional
	public Try<UserDto, Fail> findById(UserId requester, Long userId) {
		return Try.<User, Fail>from(userRepo.findOne(Specification.allOf(
						UserRepository.same(User.fromId(userId))
						//TODO some extra filters
				)), CommonFail.notFound("user " + userId))
				.map(DtoMapper.INSTANCE::toUser);
	}
	
	@Transactional
	public UserDto getSelf(UserId requester) {
		User user = userRepo.getReferenceById(requester.id());
		return DtoMapper.INSTANCE.toUser(user);
	}
}
