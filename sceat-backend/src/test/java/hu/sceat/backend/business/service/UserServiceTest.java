package hu.sceat.backend.business.service;

import hu.sceat.backend.business.dto.UserDto;
import hu.sceat.backend.util.ConfigureSpringTest;
import hu.sceat.backend.util.TestDataInitializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.Collection;

@SpringBootTest
@ConfigureSpringTest
public class UserServiceTest extends TestDataInitializer {
	
	@Autowired
	UserService userService;
	
	@Autowired
	ApplicationContext context;
	
	@BeforeEach
	void beforeEach() {
		initBasicData(context);
	}
	
	@Test
	void findById_success() {
		Assertions.assertEquals(testConsumer, userService.findById(testServer, testConsumer.id()).orElseThrow());
	}
	
	@Test
	void findById_invalid() {
		Assertions.assertTrue(userService.findById(testServer, -123L).isError());
	}
	
	@Test
	void findById_noPermission() {
		UserDto other = createConsumer(context, "TestConsumer2");
		Assertions.assertTrue(userService.findById(testConsumer, other.id()).isError());
	}
	
	@Test
	void findByEmail_success() {
		Assertions.assertEquals(testConsumer,
				userService.findByEmail(testServer, testConsumer.email()).orElseThrow());
	}
	
	@Test
	void findByEmail_invalid() {
		Assertions.assertTrue(userService.findByEmail(testServer, "wrong@email.hu").isError());
	}
	
	@Test
	void findByEmail_noPermission() {
		UserDto other = createConsumer(context, "TestConsumer2");
		Assertions.assertTrue(userService.findByEmail(testConsumer, other.email()).isError());
	}
	
	@Test
	void findByName_success() {
		Collection<UserDto> users = userService.findByName(testServer, testConsumer.name()).orElseThrow();
		Assertions.assertEquals(1, users.size());
		Assertions.assertEquals(testConsumer, users.iterator().next());
	}
	
	@Test
	void findByName_invalid() {
		Collection<UserDto> users = userService.findByName(testServer, "no one is named this").orElseThrow();
		Assertions.assertEquals(0, users.size());
	}
	
	@Test
	void findByName_noPermission() {
		UserDto other = createConsumer(context, "TestConsumer2");
		Collection<UserDto> users = userService.findByName(testConsumer, other.name()).orElseThrow();
		Assertions.assertEquals(0, users.size());
	}
	
	@Test
	void getSelf() {
		Assertions.assertEquals(testConsumer, userService.getSelf(testConsumer));
	}
	
	@Test
	void getClearFillFirstLoginFlag() {
		Assertions.assertTrue(userService.getFirstLoginFlag(testConsumer));
		userService.clearFirstLoginFlag(testConsumer);
		Assertions.assertFalse(userService.getFirstLoginFlag(testConsumer));
		userService.fillFirstLoginFlag(testConsumer);
		Assertions.assertTrue(userService.getFirstLoginFlag(testConsumer));
	}
}
