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

import java.util.Collection;

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
	public Try<Collection<UserDto>, Fail> findByName(UserId requester, String name) {
		User requesterUser = userRepo.getReferenceById(requester.id());
		return Try.success(userRepo.findAll(UserRepository.hasName(name)).stream()
				.filter(u -> hasPermissionToSee(requesterUser, u))
				.map(DtoMapper.INSTANCE::toUser)
				.toList());
	}
	
	@Transactional
	public UserDto getSelf(UserId requester) {
		User user = userRepo.getReferenceById(requester.id());
		return DtoMapper.INSTANCE.toUser(user);
	}
	
	@Transactional
	public boolean getFirstLoginFlag(UserId requester) {
		return userRepo.getReferenceById(requester.id()).getFirstLoginFlag();
	}
	
	@Transactional
	public void clearFirstLoginFlag(UserId requester) {
		User user = userRepo.getReferenceById(requester.id());
		user.clearFirstLoginFlag();
		userRepo.save(user);
	}
	
	@Transactional
	public void fillFirstLoginFlag(UserId requester) {
		User user = userRepo.getReferenceById(requester.id());
		user.fillFirstLoginFlag();
		userRepo.save(user);
	}
	
	@Transactional
	Try<User, Fail> getById(UserId requester, Long userId) {
		return getBySpecification(requester, UserRepository.same(User.fromId(userId)), "user " + userId);
	}
	
	private Try<User, Fail> getBySpecification(UserId requester, Specification<User> spec, String failMessage) {
		User requesterUser = userRepo.getReferenceById(requester.id());
		return Try.<User, Fail>from(userRepo.findOne(spec), CommonFail.notFound(failMessage))
				.filter(u -> hasPermissionToSee(requesterUser, u), CommonFail.notFound(failMessage));
	}
	
	private boolean hasPermissionToSee(User requester, User target) {
		if (requester.getId().equals(target.getId())) return true;
		if (requester.isConsumer()) return false;
		Long requesterOrg = requester.getServerProfile().orElseThrow().getOrganization().getId();
		Long userOrg = target.isServer() ? target.getServerProfile().orElseThrow().getOrganization().getId()
				: target.getConsumerProfile().orElseThrow().getOrganization().getId();
		return requesterOrg.equals(userOrg);
	}
}
