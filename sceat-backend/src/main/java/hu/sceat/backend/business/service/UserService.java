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
public class UserService {
	
	private final UserRepository userRepo;
	
	public UserService(UserRepository userRepo) {
		this.userRepo = userRepo;
	}
	
	@Transactional
	public Try<UserDto, Fail> findById(UserId requester, Long userId) {
		return getById(requester, userId)
				.map(DtoMapper.INSTANCE::toUser);
	}
	
	@Transactional
	public Try<UserDto, Fail> findByEmail(UserId requester, String email) {
		return getBySpecification(requester, UserRepository.hasEmail(email), "user email " + email)
				.map(DtoMapper.INSTANCE::toUser);
	}
	
	@Transactional
	public Try<UserDto, Fail> findByName(UserId requester, String name) {
		return getBySpecification(requester, UserRepository.hasName(name), "user name " + name)
				.map(DtoMapper.INSTANCE::toUser);
	}
	
	@Transactional
	public UserDto getSelf(UserId requester) {
		User user = userRepo.getReferenceById(requester.id());
		return DtoMapper.INSTANCE.toUser(user);
	}
	
	@Transactional
	Try<User, Fail> getById(UserId requester, Long userId) {
		return getBySpecification(requester, UserRepository.same(User.fromId(userId)), "user " + userId);
	}
	
	private Try<User, Fail> getBySpecification(UserId requester, Specification<User> spec, String failMessage) {
		return Try.from(userRepo.findOne(spec), CommonFail.notFound(failMessage));
		//TODO some extra filters (requester has permission to see the user)
		
		//TODO we assume that each user is only linked to a single organization. This is true for now,
		//  but it might change in the future.
	}
}
