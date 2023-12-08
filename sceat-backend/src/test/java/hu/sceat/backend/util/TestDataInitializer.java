package hu.sceat.backend.util;

import hu.sceat.backend.business.PrincipalUser;
import hu.sceat.backend.business.dto.MenuDto;
import hu.sceat.backend.business.dto.OrganizationDto;
import hu.sceat.backend.business.dto.UserDto;
import hu.sceat.backend.business.service.AuthService;
import hu.sceat.backend.business.service.MenuService;
import hu.sceat.backend.business.service.OrganizationService;
import hu.sceat.backend.persistence.entity.Occasion;
import hu.sceat.backend.persistence.entity.User;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public abstract class TestDataInitializer {
	
	protected OrganizationDto testOrg;
	protected UserDto testConsumer;
	protected UserDto testServer;
	
	protected void initBasicData(ApplicationContext context) {
		OrganizationService orgService = context.getBean(OrganizationService.class);
		AuthService authService = context.getBean(AuthService.class);
		
		testOrg = orgService.create("TestOrg").orElseThrow();
		testConsumer = authService.registerConsumer("test_consumer@elte.hu", "password",
				"TestConsumer", testOrg.getId()).orElseThrow();
		testServer = authService.registerServer("test_server@elte.hu", "password",
				"TestServer", testOrg.getId()).orElseThrow();
	}
	
	protected UserDto createConsumer(ApplicationContext context, String name) {
		AuthService authService = context.getBean(AuthService.class);
		String email = name.toLowerCase().replace(" ", "_") + "@elte.hu";
		return authService.registerConsumer(email, "password",
				name, testOrg.getId()).orElseThrow();
	}
	
	protected MenuDto createMenu(ApplicationContext context, String name, LocalDate date, Occasion occasion) {
		MenuService menuService = context.getBean(MenuService.class);
		return menuService.create(testServer, testOrg.getId(), name, date, occasion, 42,
				List.of("Test"), Set.of()).orElseThrow();
	}
	
	protected static RequestPostProcessor auth(UserDto userDto) {
		return auth(userDto.id(), userDto.name(), "password");
	}
	
	protected static RequestPostProcessor auth(Long id, String name, String password) {
		User user = Mockito.mock(User.class);
		Mockito.when(user.getId()).thenReturn(id);
		Mockito.when(user.getName()).thenReturn(name);
		Mockito.when(user.getPassword()).thenReturn(password);
		return SecurityMockMvcRequestPostProcessors.user(new PrincipalUser(user));
	}
}
