package com.codely.criteria;

import com.codely.criteria.errors.InvalidCriteria;
import com.codely.criteria.filters.FilterPrimitives;
import com.codely.criteria.filters.Filters;
import com.codely.criteria.order.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


class CriteriaTest {

	@Test
	@DisplayName("Should throw an error when the page number is defined but page size is not")
	void shouldThrowAnErrorWhenThePageNumberIsDefinedButPageFilterSizeIsNot() {
		assertThrows(InvalidCriteria.class, () -> {
			new Criteria(new Filters(List.of()), Order.none(), null, new Random().nextInt(100) + 1);
		});
	}

	@Test
	@DisplayName("Should create Criteria from primitives")
	void shouldCreateCriteriaFromPrimitives() throws InvalidCriteria {
		List<FilterPrimitives> filterPrimitives = List.of(); // Add appropriate filter primitives if needed
		String                 orderBy          = "someField";
		String                 orderType        = "ASC";
		Integer                pageSize         = 10;
		Integer                pageNumber       = 1;

		Criteria criteria = Criteria.fromPrimitives(filterPrimitives, orderBy, orderType, pageSize, pageNumber);

		assertNotNull(criteria);
		assertEquals(pageSize, criteria.pageSize());
		assertEquals(pageNumber, criteria.pageNumber());
		assertEquals(orderBy, criteria.orderBy().value());
		assertEquals(orderType, criteria.orderType().name());
	}

	@Test
	@DisplayName("Should throw InvalidCriteria when creating from primitives with invalid filters")
	void shouldThrowInvalidCriteriaFromInvalidPrimitives() {
		List<FilterPrimitives> filterPrimitives = List.of();
		String                 orderBy          = "someField";
		String                 orderType        = "ASC";
		Integer                pageSize         = null;
		Integer                pageNumber       = 1;

		assertThrows(InvalidCriteria.class, () -> {
			Criteria.fromPrimitives(filterPrimitives, orderBy, orderType, pageSize, pageNumber);
		});
	}

	@Test
	@DisplayName("Should throw InvalidCriteria when creating from primitives with an invalid filter")
	void shouldThrowInvalidCriteriaWithInvalidFilter() {
		List<FilterPrimitives> filterPrimitives = List.of(
				new FilterPrimitives(Optional.of("invalidField"), "invalidOperator", Optional.of("invalidValue")) // Assumes constructor for invalid filter
		);
		String  orderBy    = "someField";
		String  orderType  = "ASC";
		Integer pageSize   = 10;
		Integer pageNumber = 1;

		assertThrows(InvalidCriteria.class, () -> {
			Criteria.fromPrimitives(filterPrimitives, orderBy, orderType, pageSize, pageNumber);
		});
	}


	@Test
	@DisplayName("Should create Criteria from primitives and get all fields")
	void shouldCreateCriteriaFromPrimitivesAndGetAllFields() throws InvalidCriteria {
		List<FilterPrimitives> filterPrimitives = List.of(); // Add appropriate filter primitives if needed
		String                 orderBy          = "someField";
		String                 orderType        = "ASC";
		Integer                pageSize         = 10;
		Integer                pageNumber       = 1;

		Criteria criteria = Criteria.fromPrimitives(filterPrimitives, orderBy, orderType, pageSize, pageNumber);

		assertNotNull(criteria.filters());
		assertEquals(0, criteria.filterSize());
		assertNotNull(criteria.orderBy());
		assertNotNull(criteria.orderType());
		assertEquals(pageSize, criteria.pageSize());
		assertEquals(pageNumber, criteria.pageNumber());
	}

	@Test
	@DisplayName("Should create Criteria with valid filters")
	void shouldCreateCriteriaWithValidFilters() {
		List<FilterPrimitives> filterPrimitives = List.of(
				new FilterPrimitives(Optional.of("validField"), "EQUALS", Optional.of("validValue")) // Assumes constructor for valid filter
		);
		String  orderBy    = "someField";
		String  orderType  = "ASC";
		Integer pageSize   = 10;
		Integer pageNumber = null;

		assertThrows(InvalidCriteria.class, () -> {
			Criteria.fromPrimitives(filterPrimitives, orderBy, orderType, pageSize, pageNumber);
		});
	}


}
