package hu.sceat.backend.business.service;

import hu.sceat.backend.business.dto.ConsumerDto;
import hu.sceat.backend.business.dto.MenuDto;
import hu.sceat.backend.business.dto.UserDto;
import hu.sceat.backend.persistence.entity.Allergen;
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
import java.util.Set;

@SpringBootTest
@ConfigureSpringTest
public class ConsumerServiceTest extends TestDataInitializer {
	
	@Autowired
	private ConsumerService consumerService;
	
	@Autowired
	ApplicationContext context;
	
	@BeforeEach
	void beforeEach() {
		initBasicData(context);
	}
	
	@Test
	void findById_success() {
		ConsumerDto result = consumerService.findById(testServer, testConsumer.id()).orElseThrow();
		Assertions.assertEquals(testConsumer.consumerProfile().orElseThrow(), result);
	}
	
	@Test
	void findById_invalidId() {
		Assertions.assertTrue(consumerService.findById(testServer, -123L).isError());
	}
	
	@Test
	void findById_notConsumer() {
		Assertions.assertTrue(consumerService.findById(testServer, testServer.id()).isError());
	}
	
	@Test
	void findById_noPermission() {
		UserDto other = createConsumer(context, "TestConsumer2");
		Assertions.assertTrue(consumerService.findById(testConsumer, other.id()).isError());
	}
	
	@Test
	void getActualMenu() {
		LocalDate date = LocalDate.now().plusDays(1);
		Occasion occasion = Occasion.LUNCH;
		MenuDto menuDto = createMenu(context, "TestMenu", date, occasion);
		consumerService.addPurchasedMenu(testConsumer, menuDto.id()).orElseThrow();
		Collection<MenuDto> actual = consumerService.getActualMenu(testConsumer, testConsumer.id(),
				testOrg.id(), date, occasion).orElseThrow();
		Assertions.assertEquals(1, actual.size());
		Assertions.assertEquals(menuDto, actual.iterator().next());
	}
	
	@Test
	void addGetRemovePurchasedMenus() {
		LocalDate date = LocalDate.now().plusDays(1);

		MenuDto menuDto = createMenu(context, "TestMenu", date, Occasion.LUNCH);
		consumerService.addPurchasedMenu(testConsumer, menuDto.id()).orElseThrow();

		MenuDto future = createMenu(context, "FutureMenu", date.plusDays(3), Occasion.LUNCH);
		consumerService.addPurchasedMenu(testConsumer, future.id()).orElseThrow();
		
		Collection<MenuDto> purchased = consumerService.getPurchasedMenus(testConsumer, testConsumer.id(),
				date, date.plusDays(1)).orElseThrow();
		Assertions.assertEquals(1, purchased.size());
		Assertions.assertEquals(menuDto, purchased.iterator().next());
		
		consumerService.removePurchasedMenu(testConsumer, menuDto.id()).orElseThrow();
		purchased = consumerService.getPurchasedMenus(testConsumer, testConsumer.id(),
				date, date.plusDays(1)).orElseThrow();
		Assertions.assertEquals(0, purchased.size());
	}
	
	@Test
	void setAllergies() {
		Assertions.assertEquals(Set.of(), testConsumer.consumerProfile().orElseThrow().allergies());
		consumerService.setAllergies(testConsumer, Set.of(Allergen.FISH)).orElseThrow();
		Assertions.assertEquals(Set.of(Allergen.FISH), consumerService.findById(testConsumer, testConsumer.id())
				.orElseThrow().allergies());
	}
	
	@Test
	void getSetPhoto() {
		Assertions.assertArrayEquals(new byte[0],
				consumerService.getPhoto(testConsumer, testConsumer.id()).orElseThrow());
		consumerService.setPhoto(testConsumer, new byte[]{42}).orElseThrow();
		Assertions.assertArrayEquals(new byte[]{42},
				consumerService.getPhoto(testConsumer, testConsumer.id()).orElseThrow());
	}
}
