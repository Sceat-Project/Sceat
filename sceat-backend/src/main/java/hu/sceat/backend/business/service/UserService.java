package hu.sceat.backend.business.service;

import hu.sceat.backend.business.dto.DtoMapper;
import hu.sceat.backend.business.dto.MenuDto;
import hu.sceat.backend.business.dto.UserDto;
import hu.sceat.backend.business.fail.CommonFail;
import hu.sceat.backend.business.fail.Fail;
import hu.sceat.backend.business.id.UserId;
import hu.sceat.backend.persistence.entity.Consumer;
import hu.sceat.backend.persistence.entity.Occasion;
import hu.sceat.backend.persistence.entity.User;
import hu.sceat.backend.persistence.repository.UserRepository;
import hu.sceat.backend.util.Try;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;

@Service
public class UserService {
	
	private final UserRepository userRepo;
	private final OrganizationService orgService;
	
	public UserService(UserRepository userRepo, OrganizationService orgService) {
		this.userRepo = userRepo;
		this.orgService = orgService;
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
	public Try<Collection<MenuDto>, Fail> getMenu(UserId requester, Long userId, Long organizationId,
			LocalDate date, Occasion occasion) {
		//TODO optimize this by querying menus directly (adding the filters to the query)
		return orgService.findById(organizationId) //make sure the organization is valid
				.flatMap(o -> getById(requester, userId))
				.map(u -> u.getConsumerProfile().orElseThrow())
				.map(Consumer::getPurchasedMenus)
				.map(menus -> menus.stream()
						.filter(menu -> menu.getOrganization().getId().equals(organizationId))
						.filter(menu -> menu.getDate().isEqual(date) && menu.getOccasion().equals(occasion))
						.map(DtoMapper.INSTANCE::toMenu)
						.toList());
	}
	
	@Transactional
	public UserDto getSelf(UserId requester) {
		User user = userRepo.getReferenceById(requester.id());
		return DtoMapper.INSTANCE.toUser(user);
	}
	
	@Transactional
	public Try<Collection<MenuDto>, Fail> getPurchasedMenus(UserId requester, Long userId,
			LocalDate startDate, LocalDate endDate) {
		//TODO optimize this by querying menus directly (adding the date filter to the query)
		return getById(requester, userId)
				.map(u -> u.getConsumerProfile().orElseThrow())
				.map(Consumer::getPurchasedMenus)
				.map(menus -> menus.stream()
						.filter(menu -> (menu.getDate().isEqual(startDate) || menu.getDate().isAfter(startDate))
								&& menu.getDate().isBefore(endDate))
						.map(DtoMapper.INSTANCE::toMenu)
						.toList());
	}
	
	@Transactional
	public Try<byte[], Fail> getPhoto(UserId requester, Long userId) {
		return getById(requester, userId)
				.map(u -> u.getConsumerProfile().orElseThrow())
				.map(Consumer::getPhoto)
				.map(o -> o.orElse(null));
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
