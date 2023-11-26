package hu.sceat.backend.business.service;

import hu.sceat.backend.business.dto.ConsumerDto;
import hu.sceat.backend.business.dto.DtoMapper;
import hu.sceat.backend.business.dto.MenuDto;
import hu.sceat.backend.business.fail.CommonFail;
import hu.sceat.backend.business.fail.Fail;
import hu.sceat.backend.business.id.UserId;
import hu.sceat.backend.persistence.entity.Allergen;
import hu.sceat.backend.persistence.entity.Consumer;
import hu.sceat.backend.persistence.entity.Occasion;
import hu.sceat.backend.persistence.entity.User;
import hu.sceat.backend.persistence.repository.UserRepository;
import hu.sceat.backend.util.Try;
import hu.sceat.backend.util.Unit;
import jakarta.transaction.Transactional;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@Service
public class ConsumerService {
	
	private final UserRepository userRepo;
	private final UserService userService;
	private final OrganizationService orgService;
	private final MenuService menuService;
	
	public ConsumerService(UserRepository userRepo, UserService userService, OrganizationService orgService,
			MenuService menuService) {
		this.userRepo = userRepo;
		this.userService = userService;
		this.orgService = orgService;
		this.menuService = menuService;
	}
	
	@Transactional
	public Try<ConsumerDto, Fail> findById(UserId requester, Long userId) {
		return get(requester, userId)
				.map(DtoMapper.INSTANCE::toConsumer);
	}
	
	@Transactional
	public Try<byte[], Fail> getPhoto(UserId requester, Long userId) {
		return get(requester, userId)
				.map(Consumer::getPhoto)
				.map(o -> o.orElse(null));
	}
	
	@Transactional
	public Try<Collection<MenuDto>, Fail> getActualMenu(UserId requester, Long userId, Long organizationId,
			LocalDate date, Occasion occasion) {
		//TODO optimize this by querying menus directly (adding the filters to the query)
		return orgService.findById(organizationId) //make sure the organization is valid
				.flatMap(o -> get(requester, userId))
				.map(Consumer::getPurchasedMenus)
				.map(menus -> menus.stream()
						.filter(menu -> menu.getOrganization().getId().equals(organizationId))
						.filter(menu -> menu.getDate().isEqual(date) && menu.getOccasion().equals(occasion))
						.map(DtoMapper.INSTANCE::toMenu)
						.toList());
	}
	
	@Transactional
	public Try<Collection<MenuDto>, Fail> getPurchasedMenus(UserId requester, Long userId,
			LocalDate startDate, LocalDate endDate) {
		//TODO optimize this by querying menus directly (adding the date filter to the query)
		return get(requester, userId)
				.map(Consumer::getPurchasedMenus)
				.map(menus -> menus.stream()
						.filter(menu -> (menu.getDate().isEqual(startDate) || menu.getDate().isAfter(startDate))
								&& menu.getDate().isBefore(endDate))
						.map(DtoMapper.INSTANCE::toMenu)
						.toList());
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
	
	@Transactional
	public Try<ConsumerDto, Fail> setPhoto(UserId requester, byte[] photo) {
		return getSelf(requester)
				.map(c -> {
					c.setPhoto(photo);
					userRepo.save(c.getUser()); //TODO does this work?
					return c;
				})
				.map(DtoMapper.INSTANCE::toConsumer);
	}
	
	@Transactional
	public Try<Unit, Fail> addPurchasedMenu(UserId requester, Long menuId) {
		return getSelf(requester)
				.flatMap(c -> menuService.get(requester, menuId).map(m -> Pair.of(c, m)))
				.filter(p -> !p.getSecond().getPurchasers().contains(p.getFirst()),
						CommonFail.invalidAction("menu already purchased"))
				.filter(p -> menuService.isPurchasable(p.getSecond()),
						CommonFail.invalidAction("menu not purchasable"))
				.map(p -> {
					Consumer c = p.getFirst();
					c.addPurchasedMenu(p.getSecond());
					userRepo.save(c.getUser());
					return Unit.get();
				});
	}
	
	@Transactional
	public Try<Unit, Fail> removePurchasedMenu(UserId requester, Long menuId) {
		return getSelf(requester)
				.flatMap(c -> menuService.get(requester, menuId).map(m -> Pair.of(c, m)))
				.filter(p -> p.getSecond().getPurchasers().contains(p.getFirst()),
						CommonFail.invalidAction("menu not purchased"))
				.filter(p -> menuService.isPurchasable(p.getSecond()),
						CommonFail.invalidAction("menu not purchasable"))
				.map(p -> {
					Consumer c = p.getFirst();
					c.removePurchasedMenu(p.getSecond());
					userRepo.save(c.getUser());
					return Unit.get();
				});
	}
	
	private Try<Consumer, Fail> get(UserId requester, Long userId) {
		return userService.getById(requester, userId)
				.filter(User::isConsumer, CommonFail.invalidAction("not a consumer"))
				.map(u -> u.getConsumerProfile().orElseThrow());
	}
	
	private Try<Consumer, Fail> getSelf(UserId requester) {
		return get(requester, requester.id());
	}
}
