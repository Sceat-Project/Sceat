package hu.sceat.backend.business.service;

import hu.sceat.backend.business.dto.DtoMapper;
import hu.sceat.backend.business.dto.MenuDto;
import hu.sceat.backend.business.dto.UserRefDto;
import hu.sceat.backend.business.fail.CommonFail;
import hu.sceat.backend.business.fail.Fail;
import hu.sceat.backend.business.id.UserId;
import hu.sceat.backend.persistence.Validation;
import hu.sceat.backend.persistence.entity.Allergen;
import hu.sceat.backend.persistence.entity.Consumer;
import hu.sceat.backend.persistence.entity.Menu;
import hu.sceat.backend.persistence.entity.Occasion;
import hu.sceat.backend.persistence.entity.User;
import hu.sceat.backend.persistence.repository.MenuRepository;
import hu.sceat.backend.util.Try;
import hu.sceat.backend.util.Unit;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
public class MenuService {
	
	private final MenuRepository menuRepo;
	private final UserService userService;
	private final OrganizationService orgService;
	
	public MenuService(MenuRepository menuRepo, UserService userService, OrganizationService orgService) {
		this.menuRepo = menuRepo;
		this.userService = userService;
		this.orgService = orgService;
	}
	
	public boolean isPurchasable(Menu menu) {
		return menu.getDate().isAfter(LocalDate.now());
	}
	
	@Transactional
	public Try<MenuDto, Fail> findById(UserId requester, Long menuId) {
		return get(requester, menuId)
				.map(DtoMapper.INSTANCE::toMenu);
	}
	
	@Transactional
	public Try<Collection<UserRefDto>, Fail> getPurchasers(UserId requester, Long menuId) {
		return get(requester, menuId)
				.map(m -> m.getPurchasers().stream()
						.map(Consumer::getUser)
						.map(DtoMapper.INSTANCE::toUserRef)
						.toList());
	}
	
	@Transactional
	public Try<MenuDto, Fail> create(UserId requester, Long organizationId, String name,
			LocalDate date, Occasion occasion, int cost, List<String> foods, Set<Allergen> allergens) {
		return orgService.getWhereServer(requester, organizationId)
				.filter(o -> name.matches(Validation.GENERAL_NAME_REGEX),
						CommonFail.invalidInputFormat("name"))
				.filter(o -> cost >= 0, CommonFail.invalidInput("cost", "negative"))
				.filter(o -> !foods.isEmpty(), CommonFail.invalidInput("foods", "empty"))
				.filter(o -> menuRepo.findOne(Specification.allOf(
						MenuRepository.hasName(name),
						MenuRepository.hasDate(date),
						MenuRepository.hasOccasion(occasion)
				)).isEmpty(), CommonFail.invalidInputAlreadyTaken("name-date-occasion triplet"))
				.map(o -> Menu.create(o, name, date, occasion, cost, foods, allergens))
				.map(menuRepo::save)
				.map(DtoMapper.INSTANCE::toMenu);
	}
	
	@Transactional
	public Try<Unit, Fail> delete(UserId requester, Long menuId) {
		return getWhereServer(requester, menuId)
				.filter(m -> m.getPurchasers().isEmpty(),
						CommonFail.invalidAction("menu has purchasers"))
				.map(m -> {
					menuRepo.delete(m);
					return Unit.get();
				});
	}
	
	private Try<Menu, Fail> getWhereServer(UserId requester, Long menuId) {
		return userService.getById(requester, requester.id())
				.filter(User::isServer, CommonFail.invalidAction("not a server"))
				.flatMap(user -> get(user, menuId));
	}
	
	Try<Menu, Fail> get(UserId requester, Long menuId) {
		return userService.getById(requester, requester.id())
				.flatMap(user -> get(user, menuId));
	}
	
	private Try<Menu, Fail> get(User requester, Long menuId) {
		return Try.from(menuRepo.findOne(Specification.allOf(
				MenuRepository.same(Menu.fromId(menuId)),
				MenuRepository.hasSharedOrganization(requester)
		)), CommonFail.notFound("menu " + menuId));
	}
}
