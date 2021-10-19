package ru.task.gibdd;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.task.gibdd.config.NumberEnum;
import ru.task.gibdd.exceptions.OverNumberLimit;
import ru.task.gibdd.models.NumberRs;
import ru.task.gibdd.services.NumberService;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ContextConfiguration(initializers = {GibddApplicationTests.Initializer.class})
@Testcontainers
class GibddApplicationTests {
	@Autowired
	private NumberService numberService;

	@Container
	public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13");

	static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
			TestPropertyValues.of(
					"spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
					"spring.datasource.username=" + postgreSQLContainer.getUsername(),
					"spring.datasource.password=" + postgreSQLContainer.getPassword(),
					"spring.liquibase.enabled=true"
			).applyTo(configurableApplicationContext.getEnvironment());
		}
	}

	@Test
	@Transactional
	void countNumberInList() {
		int size = numberService.all().size();
		assertEquals(0, size);
	}

	@Test
	@Transactional
	void nextNumber() throws OverNumberLimit {
		NumberRs next = numberService.next();
		assertEquals(next.getValue(),  "A001AА " + NumberEnum.REGION.getValue() + NumberEnum.RUS.getValue());
	}

	@Test
	@Transactional
	void nextManyNumber() throws OverNumberLimit {
		for (int i = 0; i < 99; i++)
			numberService.next();
		NumberRs next = numberService.next();
		assertEquals(next.getValue(), "A100AА " + NumberEnum.REGION.getValue() + NumberEnum.RUS.getValue());
	}

	@Test
	@Transactional
	void randomAndNextNumber() throws OverNumberLimit {
		NumberRs random = numberService.random();
		NumberRs next = numberService.next();
		int size = numberService.all().size();

		assertNotEquals(random, next);
		assertEquals(random.getValue().length(), 14);
		assertEquals(next.getValue().length(), 14);
		assertEquals(size, 2);
	}
}
