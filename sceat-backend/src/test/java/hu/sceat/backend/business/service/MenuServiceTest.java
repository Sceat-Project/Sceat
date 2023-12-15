package hu.sceat.backend.business.service;

import hu.sceat.backend.business.dto.MenuDto;
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
public class MenuServiceTest extends TestDataInitializer {
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private ConsumerService consumerService;
	
	@Autowired
	ApplicationContext context;
	
	private MenuDto testMenu;
	
	@BeforeEach
	void beforeEach() {
		initBasicData(context);
		testMenu = createMenu(context, "TestMenu", LocalDate.now().plusDays(1), Occasion.LUNCH);
	}
	
	@Test
	void findById_success() {
		Assertions.assertEquals(testMenu, menuService.findById(testConsumer, testConsumer.id()).orElseThrow());
	}
	
	@Test
	void findById_invalidId() {
		Assertions.assertTrue(menuService.findById(testServer, -123L).isError());
	}
	
	@Test
	void getPurchasers() {
		Collection<UserRefDto> purchasers = menuService.getPurchasers(testConsumer, testMenu.id()).orElseThrow();
		Assertions.assertEquals(0, purchasers.size());
		
		consumerService.addPurchasedMenu(testConsumer, testMenu.id()).orElseThrow();
		purchasers = menuService.getPurchasers(testConsumer, testMenu.id()).orElseThrow();
		Assertions.assertEquals(1, purchasers.size());
		Assertions.assertEquals(testConsumer.id(), purchasers.iterator().next().id());
		
		consumerService.removePurchasedMenu(testConsumer, testMenu.id()).orElseThrow();
		purchasers = menuService.getPurchasers(testConsumer, testMenu.id()).orElseThrow();
		Assertions.assertEquals(0, purchasers.size());
	}
	
	@Test
	void delete() {
		menuService.delete(testServer, testMenu.id()).orElseThrow();
		Assertions.assertTrue(menuService.findById(testServer, testMenu.id()).isError());
	}
}
