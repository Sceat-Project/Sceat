package hu.sceat.backend.app;

import hu.sceat.backend.business.service.AuthService;
import hu.sceat.backend.business.service.UserService;
import hu.sceat.backend.util.ConfigureSpringTest;
import hu.sceat.backend.util.TestDataInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ConfigureSpringTest
@AutoConfigureMockMvc
public class WebSecurityIntTest extends TestDataInitializer {
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ApplicationContext context;
	
	@BeforeEach
	void beforeEach() {
		initBasicData(context);
	}
	
	@Test
	void registerConsumerSuccess() throws Exception {
		mvc.perform(post("/api/auth/register/consumer")
						.with(csrf())
						.param("email", "email@elte.hu")
						.param("password", "password")
						.param("name", "name")
						.param("organization", testOrg.id().toString()))
				.andExpect(status().isOk());
		
		userService.findByName(testServer, "name").orElseThrow();
	}
	
	@Test
	void registerServerSuccess() {
		authService.registerServer("email@elte.hu", "password", "name", testOrg.id())
				.orElseThrow();
		userService.findByName(testServer, "name").orElseThrow();
	}
	
	@Test
	void registerEmailTaken() throws Exception {
		mvc.perform(post("/api/auth/register/consumer")
						.with(csrf())
						.param("email", testConsumer.email())
						.param("password", "password")
						.param("name", "name")
						.param("organization", testOrg.id().toString()))
				.andExpect(status().is4xxClientError());
	}
	
	@Test
	void registerInvalidName() throws Exception {
		mvc.perform(post("/api/auth/register/consumer")
						.with(csrf())
						.param("email", "email@elte.hu")
						.param("password", "password")
						.param("name", "-")
						.param("organization", testOrg.id().toString()))
				.andExpect(status().is4xxClientError());
	}
	
	@Test
	void loginSuccess() throws Exception {
		mvc.perform(formLogin("/api/auth/login")
						.userParameter("email")
						.user(testConsumer.email())
						.password("password"))
				.andExpect(status().isOk());
	}
	
	@Test
	void loginWrongPassword() throws Exception {
		mvc.perform(formLogin("/api/auth/login")
						.userParameter("email")
						.user(testConsumer.email())
						.password("wrong_password"))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void loginNonExistentUser() throws Exception {
		mvc.perform(formLogin("/api/auth/login")
						.userParameter("email")
						.user("wrong@email.hu")
						.password("password"))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void changePassword() throws Exception {
		mvc.perform(post("/api/auth/password")
						.with(csrf())
						.with(auth(testConsumer))
						.param("password", "new_password"))
				.andExpect(status().isOk());
		
		mvc.perform(formLogin("/api/auth/login")
						.userParameter("email")
						.user(testConsumer.email())
						.password("new_password"))
				.andExpect(status().isOk());
	}
	
	@Test
	void logout() throws Exception {
		mvc.perform(SecurityMockMvcRequestBuilders.logout("/api/auth/logout"))
				.andExpect(status().isOk());
	}
	
	@Test
	void notAuthenticated() throws Exception {
		mvc.perform(get("/api/user/self"))
				.andExpect(status().isUnauthorized());
	}
}
