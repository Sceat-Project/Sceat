package hu.sceat.backend.business.service;

import hu.sceat.backend.business.dto.DtoMapper;
import hu.sceat.backend.business.dto.MenuDto;
import hu.sceat.backend.business.fail.CommonFail;
import hu.sceat.backend.business.fail.Fail;
import hu.sceat.backend.business.id.UserId;
import hu.sceat.backend.persistence.Validation;
import hu.sceat.backend.persistence.entity.Allergen;
import hu.sceat.backend.persistence.entity.Menu;
import hu.sceat.backend.persistence.entity.Occasion;
import hu.sceat.backend.persistence.entity.User;
import hu.sceat.backend.persistence.repository.MenuRepository;
import hu.sceat.backend.util.Try;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class MenuService {
	
	private final MenuRepository menuRepo;
	private final OrganizationService orgService;
	
	public MenuService(MenuRepository menuRepo, OrganizationService orgService) {
		this.menuRepo = menuRepo;
		this.orgService = orgService;
	}
	
	@Transactional
	public Try<MenuDto, Fail> findById(UserId requester, Long menuId) {
		return Try.<Menu, Fail>from(menuRepo.findOne(Specification.allOf(
						MenuRepository.same(Menu.fromId(menuId)),
						MenuRepository.hasSharedOrganization(User.fromId(requester.id()))
				)), CommonFail.notFound("menu " + menuId))
				.map(DtoMapper.INSTANCE::toMenu);
	}
	
	@Transactional
	public Try<MenuDto, Fail> create(UserId requester, Long organizationId, String name,
			LocalDate date, Occasion occasion, int cost, List<String> foods, Set<Allergen> allergens) {
		return orgService.resolveOrgWhereServer(requester, organizationId)
				.filter(o -> name.matches(Validation.GENERAL_NAME_REGEX),
						CommonFail.invalidInputFormat("name"))
				.filter(o -> cost >= 0, CommonFail.invalidInput("cost", "negative"))
				.filter(o -> !foods.isEmpty(), CommonFail.invalidInput("foods", "empty"))
				.map(o -> Menu.create(o, name, date, occasion, cost, foods, allergens))
				.map(menuRepo::save)
				.map(DtoMapper.INSTANCE::toMenu);
	}
}
