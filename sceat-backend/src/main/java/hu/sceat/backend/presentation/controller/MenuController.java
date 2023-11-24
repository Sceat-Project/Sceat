package hu.sceat.backend.presentation.controller;

import hu.sceat.backend.business.PrincipalUser;
import hu.sceat.backend.business.dto.MenuDto;
import hu.sceat.backend.business.service.MenuService;
import hu.sceat.backend.persistence.entity.Allergen;
import hu.sceat.backend.persistence.entity.Occasion;
import hu.sceat.backend.presentation.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/menu")
public class MenuController {
	
	private final MenuService menuService;
	
	public MenuController(MenuService menuService) {
		this.menuService = menuService;
	}
	
	@GetMapping("/id/{menuId}")
	public ResponseEntity<MenuDto> get(PrincipalUser principal, @PathVariable Long menuId) {
		return menuService.findById(principal, menuId)
				.get(ResponseUtil::respondOk, ResponseUtil::respondFail);
	}
	
	@GetMapping("/create")
	public ResponseEntity<MenuDto> get(PrincipalUser principal, @RequestParam Long organizationId,
			@RequestParam String name, @RequestParam LocalDate date, @RequestParam Occasion occasion,
			@RequestParam int cost, @RequestParam List<String> foods, @RequestParam Set<Allergen> allergens) {
		return menuService.create(principal, organizationId, name, date, occasion, cost, foods, allergens)
				.get(ResponseUtil::respondOk, ResponseUtil::respondFail);
	}
}
