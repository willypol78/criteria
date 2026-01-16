package io.github.willypol78.criteria.from;

import io.github.willypol78.criteria.Criteria;
import io.github.willypol78.criteria.errors.InvalidCriteria;
import io.github.willypol78.criteria.order.OrderType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.Random.class)
class CriteriaFromHttpServletRequestDqlConverterTest {

	@Test
	@DisplayName("Should convert one filter to criteria payload DQL")
	void shouldConvertsOneFilterToCriteria() throws InvalidCriteria, InvalidDQL {
		// Given
		CriteriaFromHttpServletRequestDqlConverter converter = new CriteriaFromHttpServletRequestDqlConverter();

		MockHttpServletRequest mockRequest = new MockHttpServletRequest();
		mockRequest.setRequestURI("/api/v1/users");
		mockRequest.setContentType("application/json");
		String jsonPayload = """
				   [
				            {
				                "field": "event",
				                "operator": "=",
				                "value": "1"
				            }
				        ]
				""";
		mockRequest.setContent(jsonPayload.getBytes());

		// When
		Criteria criteria = converter.toCriteria(mockRequest);

		// Then
		assertEquals(1, criteria.filterSize());
		assertEquals("", criteria.orderBy().value());
		assertEquals(OrderType.NONE, criteria.orderType());
		assertNull(criteria.pageSize());
	}

	@Test
	@DisplayName("Should convert complex criteria from request payload DQL")
	void shouldConvertComplexCriteriaFromRequest() throws InvalidCriteria, InvalidDQL {
		// Given
		CriteriaFromHttpServletRequestDqlConverter converter = new CriteriaFromHttpServletRequestDqlConverter();

		MockHttpServletRequest mockRequest = new MockHttpServletRequest();
		mockRequest.setRequestURI("/api/v1/users");
		mockRequest.setContentType("application/json");
		String jsonPayload = """
				[
					{
						"field": "event",
						"operator": "=",
						"value": "1"
					},
					{ "operator": "and" },
					{ "operator": "not" },
					{ "operator": "(" },
					{
						"field": "event",
						"operator": "=",
						"value": "2"
					},
					{ "operator": ")" }
				]
				""";
		mockRequest.setContent(jsonPayload.getBytes());

		// When
		Criteria criteria = converter.toCriteria(mockRequest);

		// Then
		assertEquals(6, criteria.filterSize());
		assertEquals("", criteria.orderBy().value());
		assertEquals(OrderType.NONE, criteria.orderType());
		assertNull(criteria.pageSize());
	}

	@Test
	@DisplayName("Should throw InvalidDQL exception when invalid payload DQL")
	void shouldThrowInvalidDQLExceptionWhenInvalidPayload() {
		// Given
		CriteriaFromHttpServletRequestDqlConverter converter = new CriteriaFromHttpServletRequestDqlConverter();

		MockHttpServletRequest mockRequest = new MockHttpServletRequest();
		mockRequest.setRequestURI("/api/v1/users");
		mockRequest.setContentType("application/json");
		String jsonPayload = """	
				{Invalid payload}
				""";
		mockRequest.setContent(jsonPayload.getBytes());

		// When & Then
		assertThrows(InvalidDQL.class, () -> converter.toCriteria(mockRequest));
	}

}
