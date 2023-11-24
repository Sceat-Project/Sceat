package hu.sceat.backend.presentation.controller;

import hu.sceat.backend.business.PrincipalUser;
import hu.sceat.backend.business.dto.MenuDto;
import hu.sceat.backend.business.dto.OrganizationDto;
import hu.sceat.backend.business.dto.UserRefDto;
import hu.sceat.backend.business.service.OrganizationService;
import hu.sceat.backend.presentation.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Collection;

@RestController
@RequestMapping("/api/organization")
public class OrganizationController {
	
	private final OrganizationService orgService;
	
	public OrganizationController(OrganizationService orgService) {
		this.orgService = orgService;
	}
	
	@GetMapping("/id/{organizationId}")
	public ResponseEntity<OrganizationDto> get(@PathVariable Long organizationId) {
		return orgService.findById(organizationId)
				.get(ResponseUtil::respondOk, ResponseUtil::respondFail);
	}
	
	@GetMapping("/id/{organizationId}/menus")
	public ResponseEntity<Collection<MenuDto>> menus(PrincipalUser principal,
			@PathVariable Long organizationId, @RequestParam LocalDate startDate,
			@RequestParam LocalDate endDate) {
		return orgService.listMenus(principal, organizationId, startDate, endDate)
				.get(ResponseUtil::respondOk, ResponseUtil::respondFail);
	}
	
	@GetMapping("/id/{organizationId}/servers")
	public ResponseEntity<Collection<UserRefDto>> servers(PrincipalUser principal,
			@PathVariable Long organizationId) {
		return orgService.listServers(principal, organizationId)
				.get(ResponseUtil::respondOk, ResponseUtil::respondFail);
	}
	
	@GetMapping("/id/{organizationId}/consumers")
	public ResponseEntity<Collection<UserRefDto>> consumers(PrincipalUser principal,
			@PathVariable Long organizationId) {
		return orgService.listConsumers(principal, organizationId)
				.get(ResponseUtil::respondOk, ResponseUtil::respondFail);
	}
}
