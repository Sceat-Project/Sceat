package hu.sceat.backend.business.service;

import hu.sceat.backend.business.dto.ConsumerDto;
import hu.sceat.backend.business.dto.DtoMapper;
import hu.sceat.backend.business.fail.CommonFail;
import hu.sceat.backend.business.fail.Fail;
import hu.sceat.backend.business.id.UserId;
import hu.sceat.backend.persistence.entity.Allergen;
import hu.sceat.backend.persistence.entity.Consumer;
import hu.sceat.backend.persistence.entity.User;
import hu.sceat.backend.persistence.repository.UserRepository;
import hu.sceat.backend.util.Try;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ConsumerService {
	
	private final UserRepository userRepo;
	private final UserService userService;
	
	public ConsumerService(UserRepository userRepo, UserService userService) {
		this.userRepo = userRepo;
		this.userService = userService;
	}
	
	@Transactional
	public Try<ConsumerDto, Fail> findById(UserId requester, Long userId) {
		return get(requester, userId)
				.map(DtoMapper.INSTANCE::toConsumer);
	}
	
	@Transactional
	public Try<ConsumerDto, Fail> setAllergies(UserId requester, Set<Allergen> allergies) {
		return getSelf(requester)
				.map(c -> {
					c.setAllergies(allergies);
					userRepo.save(c.getUser()); //TODO does this work?
					return c;
				})
				.map(DtoMapper.INSTANCE::toConsumer);
	}
	
	//TODO method to modify photo (if it's mentioned in the MVP)
	
	private Try<Consumer, Fail> get(UserId requester, Long userId) {
		return userService.getById(requester, userId)
				.filter(User::isConsumer, CommonFail.invalidAction("not a consumer"))
				.map(u -> u.getConsumerProfile().orElseThrow());
	}
	
	private Try<Consumer, Fail> getSelf(UserId requester) {
		return get(requester, requester.id());
	}
}
