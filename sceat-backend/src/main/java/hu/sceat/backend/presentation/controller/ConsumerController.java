package hu.sceat.backend.presentation.controller;

import hu.sceat.backend.business.PrincipalUser;
import hu.sceat.backend.business.dto.ConsumerDto;
import hu.sceat.backend.business.dto.MenuDto;
import hu.sceat.backend.business.service.ConsumerService;
import hu.sceat.backend.persistence.entity.Allergen;
import hu.sceat.backend.persistence.entity.Occasion;
import hu.sceat.backend.presentation.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@RestController
@RequestMapping("/api/consumer")
public class ConsumerController {
	
	private final ConsumerService consumerService;
	
	public ConsumerController(ConsumerService consumerService) {
		this.consumerService = consumerService;
	}
	
	@GetMapping("/id/{userId}")
	public ResponseEntity<ConsumerDto> get(PrincipalUser principal, @PathVariable Long userId) {
		return consumerService.findById(principal, userId)
				.get(ResponseUtil::respondOk, ResponseUtil::respondFail);
	}
	
	@GetMapping("/id/{userId}/photo")
	public ResponseEntity<byte[]> getPhoto(PrincipalUser principal, @PathVariable Long userId) {
		return consumerService.getPhoto(principal, userId)
				.get(ResponseUtil::respondOk, ResponseUtil::respondFail);
	}
	
	@GetMapping("/id/{userId}/actualMenu")
	public ResponseEntity<Collection<MenuDto>> getActualMenu(PrincipalUser principal, @PathVariable Long userId,
			@RequestParam Long organizationId, @RequestParam LocalDate date, @RequestParam Occasion occasion) {
		return consumerService.getActualMenu(principal, userId, organizationId, date, occasion)
				.get(ResponseUtil::respondOk, ResponseUtil::respondFail);
	}
	
	@GetMapping("/self/purchasedMenus")
	public ResponseEntity<Collection<MenuDto>> getSelfPurchasedMenus(PrincipalUser principal,
			@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
		return consumerService.getPurchasedMenus(principal, principal.id(), startDate, endDate)
				.get(ResponseUtil::respondOk, ResponseUtil::respondFail);
	}
	
	@PostMapping("/self/allergies")
	public ResponseEntity<ConsumerDto> postSelfAllergies(PrincipalUser principal,
			@RequestParam Set<Allergen> allergies) {
		return consumerService.setAllergies(principal, allergies)
				.get(ResponseUtil::respondOk, ResponseUtil::respondFail);
	}
}
