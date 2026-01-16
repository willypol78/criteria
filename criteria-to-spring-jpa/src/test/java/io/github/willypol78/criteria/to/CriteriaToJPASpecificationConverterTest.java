package io.github.willypol78.criteria.to;

import io.github.willypol78.criteria.Criteria;
import io.github.willypol78.criteria.errors.InvalidCriteria;
import io.github.willypol78.criteria.filters.ComparatorFilter;
import io.github.willypol78.criteria.filters.Filter;
import io.github.willypol78.criteria.filters.Filters;
import io.github.willypol78.criteria.operators.ComparatorFilterOperator;
import io.github.willypol78.criteria.order.Order;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;

import static io.github.willypol78.criteria.filters.Filter.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class CriteriaToJPASpecificationConverterTest {

	@Container
	private final static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16.0");

	@Autowired
	private UsersTestRepository usersTestRepository;

	@DynamicPropertySource
	public static void overrideProps(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", container::getJdbcUrl);
		registry.add("spring.datasource.username", container::getUsername);
		registry.add("spring.datasource.password", container::getPassword);
		registry.add("spring.jpa.show-sql", () -> Boolean.TRUE);
	}

	@Test
	@DisplayName("Debe cargar los beans")
	void shouldLoadBeans() {
		// Given When Then
		assertThat(usersTestRepository).isNotNull();
	}

	@Test
	@DisplayName("Should test private constructor")
	void shouldTestPrivateConstructor() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
		// Given
		Constructor<CriteriaToJPASpecificationConverterTest> constructor = CriteriaToJPASpecificationConverterTest.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		// When
		CriteriaToJPASpecificationConverterTest instance = constructor.newInstance();
		// Then
		AssertionsForClassTypes.assertThat(instance).isNotNull();
	}

	@Test
	@DisplayName("Should return the elements with attempts >= 50")
	@Sql("/db/test.sql")
	void shouldReturnTheElementsWithAttemptsGreaterThan() throws InvalidCriteria {
		// Given
		Filter       f1         = new ComparatorFilter("attempts", ComparatorFilterOperator.GTE, 50);
		List<Filter> expression = List.of(f1);

		Filters filters = new Filters(expression);

		Integer  page     = 0;
		Integer  size     = 2;
		Order    order    = Order.none();
		Criteria criteria = new Criteria(filters, order, size, page);

		PostgreSQLUsersTestRepository postgreSQLUsersTestRepository = new PostgreSQLUsersTestRepository(usersTestRepository);

		// When
		Page<UserTestEntity> entityPage = postgreSQLUsersTestRepository.search(criteria);

		// Then
		assertEquals(3, entityPage.getTotalElements());
		assertEquals(2, entityPage.getContent().size());
		assertEquals(2, entityPage.getTotalPages());
	}

	@Test
	@DisplayName("Should return simple criteria with equals UUID query: \"81abaa90-2f91-11ee-be56-0242ac120002\"")
	@Sql("/db/test.sql")
	void shouldReturnSimpleCriteriaWithEqualsUUIDQuery() throws InvalidCriteria {
		// Given
		Filter       f1         = new ComparatorFilter("id", ComparatorFilterOperator.EQUAL, UUID.fromString("81abaa90-2f91-11ee-be56-0242ac120002"));
		List<Filter> expression = List.of(f1);

		Filters  filters  = new Filters(expression);
		Integer  page     = 0;
		Integer  size     = 20;
		Order    order    = Order.none();
		Criteria criteria = new Criteria(filters, order, size, page);

		PostgreSQLUsersTestRepository postgreSQLUsersTestRepository = new PostgreSQLUsersTestRepository(usersTestRepository);

		// When
		Page<UserTestEntity> entityPage = postgreSQLUsersTestRepository.search(criteria);

		// Then
		assertEquals(1, entityPage.getTotalElements());
		assertEquals(1, entityPage.getContent().size());
		assertEquals(1, entityPage.getTotalPages());
	}

	@Test
	@DisplayName("Should return simple criteria with equals query and return ignoring case")
	@Sql("/db/test.sql")
	void shouldExecuteSimpleCriteriaWithEqualsQueryAndReturnIgnoringCase() throws InvalidCriteria {
		// Given
		Filter       f1         = new ComparatorFilter("name", ComparatorFilterOperator.EQUAL, "GUIllermo");
		List<Filter> expression = List.of(f1);

		Filters filters = new Filters(expression);
		Integer page    = 0;
		Integer size    = 20;
		Order   order   = Order.none();

		Criteria criteria = new Criteria(filters, order, size, page);

		PostgreSQLUsersTestRepository postgreSQLUsersTestRepository = new PostgreSQLUsersTestRepository(usersTestRepository);

		// When
		Page<UserTestEntity> entityPage = postgreSQLUsersTestRepository.search(criteria);

		// Then
		assertEquals(1, entityPage.getTotalElements());
	}

	@Test
	@DisplayName("Should return the elements that match with regexp criteria")
	@Sql("/db/test.sql")
	void shouldReturnTheElementsThatMatchWithRegexpCriteria() throws InvalidCriteria {
		// Given
		Filter       f1         = new ComparatorFilter("name", ComparatorFilterOperator.REG_EXP, "^Gui.*$");
		List<Filter> expression = List.of(f1);

		Filters filters = new Filters(expression);
		Integer page    = 0;
		Integer size    = 20;
		Order   order   = Order.none();

		Criteria criteria = new Criteria(filters, order, size, page);

		PostgreSQLUsersTestRepository postgreSQLUsersTestRepository = new PostgreSQLUsersTestRepository(usersTestRepository);

		// When
		Page<UserTestEntity> entityPage = postgreSQLUsersTestRepository.search(criteria);

		// Then
		assertEquals(1, entityPage.getTotalElements());
	}

	@Test
	@DisplayName("Should return the two elements that match with criteria CONTAINS, GTE")
	@Sql("/db/test.sql")
	void shouldReturnTheTwoElementsThatMatchWithCriteria() throws InvalidCriteria {
		// Given
		Filter       f1         = new ComparatorFilter("attempts", ComparatorFilterOperator.GTE, 50);
		Filter       f2         = new ComparatorFilter("name", ComparatorFilterOperator.CONTAINS, "aco");
		Filter       f3         = new ComparatorFilter("name", ComparatorFilterOperator.CONTAINS, "avi");
		List<Filter> expression = List.of(f1, and(), openParenthesis(), f2, or(), f3, closeParenthesis());

		Filters filters = new Filters(expression);
		Integer page    = 0;
		Integer size    = 20;
		Order   order   = Order.asc("enabled");

		Criteria criteria = new Criteria(filters, order, size, page);

		PostgreSQLUsersTestRepository postgreSQLUsersTestRepository = new PostgreSQLUsersTestRepository(usersTestRepository);

		// When
		Page<UserTestEntity> entityPage = postgreSQLUsersTestRepository.search(criteria);

		// Then
		assertEquals(2, entityPage.getTotalElements());
	}

	@Test
	@DisplayName("Should return the element that match with criteria with NotEqual, LT y GT")
	@Sql("/db/test.sql")
	void shouldReturnTheElementThatMatchWithCriteria_NotEqual_LT_GT() throws InvalidCriteria {
		// Given
		Filter       f1         = new ComparatorFilter("enabled", ComparatorFilterOperator.NOT_EQUAL, true);
		Filter       f2         = new ComparatorFilter("attempts", ComparatorFilterOperator.GT, 25);
		Filter       f3         = new ComparatorFilter("attempts", ComparatorFilterOperator.LT, 100);
		List<Filter> expression = List.of(f1, and(), openParenthesis(), f2, and(), f3, closeParenthesis());

		Filters filters = new Filters(expression);
		Integer page    = 0;
		Integer size    = 20;
		Order   order   = Order.desc("enabled");

		Criteria criteria = new Criteria(filters, order, size, page);

		PostgreSQLUsersTestRepository postgreSQLUsersTestRepository = new PostgreSQLUsersTestRepository(usersTestRepository);

		// When
		Page<UserTestEntity> entityPage = postgreSQLUsersTestRepository.search(criteria);

		// Then
		assertEquals(1, entityPage.getTotalElements());
	}

	@Test
	@DisplayName("Should return the element that match with criteria with braces")
	@Sql("/db/test.sql")
	void shouldReturnTheElementThatMatchWithCriteriaWithBraces() throws InvalidCriteria {
		// Given
		Filter       f1         = new ComparatorFilter("name", ComparatorFilterOperator.NOT_EQUAL, "name");
		Filter       f2         = new ComparatorFilter("attempts", ComparatorFilterOperator.NOT_EQUAL, 50);
		Filter       f3         = new ComparatorFilter("attempts", ComparatorFilterOperator.NOT_EQUAL, 100L);
		Filter       f4         = new ComparatorFilter("surname", ComparatorFilterOperator.CONTAINS, "aste");
		Filter       f5         = new ComparatorFilter("enabled", ComparatorFilterOperator.NOT_EQUAL, true);
		List<Filter> expression = List.of(f1, and(), openParenthesis(), f2, and(), f3, and(), f4, or(), f5, closeParenthesis());

		Filters filters = new Filters(expression);
		Integer page    = 0;
		Integer size    = 20;
		Order   order   = Order.none();

		Criteria criteria = new Criteria(filters, order, size, page);

		PostgreSQLUsersTestRepository postgreSQLUsersTestRepository = new PostgreSQLUsersTestRepository(usersTestRepository);

		// When
		Page<UserTestEntity> entityPage = postgreSQLUsersTestRepository.search(criteria);

		// Then
		assertEquals(2, entityPage.getTotalElements());
	}

	@Test
	@DisplayName("Should return the element that match with criteria not enabled")
	@Sql("/db/test.sql")
	void shouldReturnTheElementThatMatchWithCriteriaNotEnabled() throws InvalidCriteria {
		// Given
		Filter       f1         = new ComparatorFilter("enabled", ComparatorFilterOperator.EQUAL, true);
		List<Filter> expression = List.of(not(), f1);

		Filters filters = new Filters(expression);
		Integer page    = 0;
		Integer size    = 20;
		Order   order   = Order.none();

		Criteria criteria = new Criteria(filters, order, size, page);

		PostgreSQLUsersTestRepository postgreSQLUsersTestRepository = new PostgreSQLUsersTestRepository(usersTestRepository);

		// When
		Page<UserTestEntity> entityPage = postgreSQLUsersTestRepository.search(criteria);

		// Then
		assertEquals(2, entityPage.getTotalElements());
	}

}
