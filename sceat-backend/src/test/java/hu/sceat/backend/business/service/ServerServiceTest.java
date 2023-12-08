package hu.sceat.backend.business.service;

import hu.sceat.backend.util.ConfigureSpringTest;
import hu.sceat.backend.util.TestDataInitializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
@ConfigureSpringTest
public class ServerServiceTest extends TestDataInitializer {
	
	@Autowired
	ServerService serverService;
	
	@Autowired
	ApplicationContext context;
	
	@BeforeEach
	void beforeEach() {
		initBasicData(context);
	}
	
	@Test
	void findById_success() {
		Assertions.assertEquals(testServer.serverProfile().orElseThrow(),
				serverService.findById(testServer, testServer.id()).orElseThrow());
	}
	
	@Test
	void findById_invalidId() {
		Assertions.assertTrue(serverService.findById(testServer, -123L).isError());
	}
	
	@Test
	void findById_notConsumer() {
		Assertions.assertTrue(serverService.findById(testConsumer, testConsumer.id()).isError());
	}
	
	@Test
	void findById_noPermission() {
		Assertions.assertTrue(serverService.findById(testConsumer, testServer.id()).isError());
	}
}
