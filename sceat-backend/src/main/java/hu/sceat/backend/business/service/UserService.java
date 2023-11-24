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
		return find(requester, UserRepository.same(User.fromId(userId)), "user " + userId);
	}
	
	@Transactional
	public Try<UserDto, Fail> findByEmail(UserId requester, String email) {
		return find(requester, UserRepository.hasEmail(email), "user email " + email);
	}
	
	@Transactional
	public Try<UserDto, Fail> findByName(UserId requester, String name) {
		return find(requester, UserRepository.hasName(name), "user name " + name);
	}
	
	@Transactional
	public UserDto getSelf(UserId requester) {
		User user = userRepo.getReferenceById(requester.id());
		return DtoMapper.INSTANCE.toUser(user);
	}
	
	private Try<UserDto, Fail> find(UserId requester, Specification<User> spec, String failMessage) {
		return Try.<User, Fail>from(userRepo.findOne(spec), CommonFail.notFound(failMessage))
				//TODO some extra filters
				.map(DtoMapper.INSTANCE::toUser);
	}
}
