package hu.sceat.backend.business.service;

import hu.sceat.backend.business.dto.MenuCountDto;
import hu.sceat.backend.business.dto.MenuDto;
import hu.sceat.backend.business.dto.OrganizationRefDto;
import hu.sceat.backend.business.dto.UserRefDto;
import hu.sceat.backend.persistence.entity.Occasion;
import hu.sceat.backend.util.ConfigureSpringTest;
import hu.sceat.backend.util.TestDataInitializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;
import java.util.Collection;

@SpringBootTest
@ConfigureSpringTest
public class OrganizationServiceTest extends TestDataInitializer {
	
	@Autowired
	OrganizationService orgService;
	
	@Autowired
	ConsumerService consumerService;
	
	@Autowired
	ApplicationContext context;
	
	@BeforeEach
	void beforeEach() {
		initBasicData(context);
	}
	
	@Test
	void findById_success() {
		Assertions.assertEquals(testOrg, orgService.findById(testOrg.id()).orElseThrow());
	}
	
	@Test
	void findById_invalidId() {
		Assertions.assertTrue(orgService.findById(-123L).isError());
	}
	
	@Test
	void list() {
		Collection<OrganizationRefDto> orgs = orgService.list();
		Assertions.assertEquals(1, orgs.size());
		Assertions.assertEquals(testOrg.id(), orgs.iterator().next().id());
	}
	
	@Test
	void listMenus() {
		LocalDate date = LocalDate.now();
		MenuDto menu = createMenu(context, "TestMenu", date, Occasion.LUNCH);
		createMenu(context, "FutureMenu", date.plusDays(3), Occasion.LUNCH);
		
		Collection<MenuDto> menus = orgService.listMenus(testConsumer, testOrg.id(),
				date, date.plusDays(1)).orElseThrow();
		Assertions.assertEquals(1, menus.size());
		Assertions.assertEquals(menu.id(), menus.iterator().next().id());
	}
	
	@Test
	void getMenuPurchaseCounts() {
		LocalDate date = LocalDate.now().plusDays(1);
		MenuDto menu = createMenu(context, "TestMenu", date, Occasion.LUNCH);
		createMenu(context, "FutureMenu", date.plusDays(3), Occasion.LUNCH);
		
		Collection<MenuCountDto> menuCounts = orgService.getMenuPurchaseCounts(testServer, testOrg.id(),
				date, date.plusDays(1)).orElseThrow();
		Assertions.assertEquals(1, menuCounts.size());
		Assertions.assertEquals(0, menuCounts.iterator().next().count());
		Assertions.assertEquals(menu.id(), menuCounts.iterator().next().menu().id());
		
		consumerService.addPurchasedMenu(testConsumer, menu.id()).orElseThrow();
		
		menuCounts = orgService.getMenuPurchaseCounts(testServer, testOrg.id(),
				date, date.plusDays(1)).orElseThrow();
		Assertions.assertEquals(1, menuCounts.size());
		Assertions.assertEquals(1, menuCounts.iterator().next().count());
		Assertions.assertEquals(menu.id(), menuCounts.iterator().next().menu().id());
	}
	
	@Test
	void listServers() {
		Collection<UserRefDto> servers = orgService.listServers(testServer, testOrg.id()).orElseThrow();
		Assertions.assertEquals(1, servers.size());
		Assertions.assertEquals(testServer.id(), servers.iterator().next().id());
	}
	
	@Test
	void listConsumers() {
		Collection<UserRefDto> consumers = orgService.listConsumers(testServer, testOrg.id()).orElseThrow();
		Assertions.assertEquals(1, consumers.size());
		Assertions.assertEquals(testConsumer.id(), consumers.iterator().next().id());
	}
	
	@Test
	void delete() {
		orgService.delete(testOrg.id());
		Assertions.assertTrue(orgService.findById(testOrg.id()).isError());
	}
}
