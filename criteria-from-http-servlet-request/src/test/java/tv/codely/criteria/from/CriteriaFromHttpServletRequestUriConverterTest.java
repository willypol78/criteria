package tv.codely.criteria.from;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import tv.codely.criteria.Criteria;
import tv.codely.criteria.errors.InvalidCriteria;
import tv.codely.criteria.order.OrderType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestMethodOrder(MethodOrderer.Random.class)
class CriteriaFromHttpServletRequestUriConverterTest {

	@Test
	@DisplayName("Should convert one filter to criteria")
	void shouldConvertsOneFilterToCriteria() throws InvalidCriteria {
		// Given
		CriteriaFromHttpServletRequestUriConverter converter = new CriteriaFromHttpServletRequestUriConverter();

		MockHttpServletRequest mockRequest = new MockHttpServletRequest();
		mockRequest.setRequestURI("/api/v1/users?f[0][f]=name&f[0][o]=%3D&f[0][v]=John");

		// When
		Criteria criteria = converter.toCriteria(mockRequest);

		// Then
		assertEquals(1, criteria.filterSize());
		assertEquals("", criteria.orderBy().value());
		assertEquals(OrderType.NONE, criteria.orderType());
		assertNull(criteria.pageSize());
	}

	@Test
	@DisplayName("Should convert complex criteria from request")
	void shouldConvertComplexCriteriaFromRequest() throws InvalidCriteria {
		// Given
		CriteriaFromHttpServletRequestUriConverter converter = new CriteriaFromHttpServletRequestUriConverter();

		// DQL: name = "John" AND NOT(age < 18 OR age > 65)
		MockHttpServletRequest mockRequest = new MockHttpServletRequest();
		mockRequest.setRequestURI(
				"/api/v1/users?f[0][f]=name&f[0][o]=%3D&f[0][v]=John&f[1][o]=AND&f[2][o]=NOT&f[3][o]=(&f[4][f]=age&f[4][o]=%3C&f[4][v]=18&f[5][o]=OR&f[6][f]=age&f[6][o]=%3E&f[6][v]=65&f[7][o]=)&orderBy=name&orderType=ASC&page=1&size=10"
		);

		// When
		Criteria criteria = converter.toCriteria(mockRequest);

		// Then
		assertEquals(8, criteria.filterSize());
		assertEquals("name", criteria.orderBy().value());
		assertEquals(OrderType.ASC, criteria.orderType());
		assertEquals(10, criteria.pageSize());
		assertEquals(1, criteria.pageNumber());
	}

}
