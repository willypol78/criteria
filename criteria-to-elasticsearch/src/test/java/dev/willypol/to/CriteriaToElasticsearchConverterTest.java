package dev.willypol.to;

import dev.willypol.Criteria;
import dev.willypol.CriteriaMother;
import dev.willypol.errors.InvalidCriteria;
import dev.willypol.errors.InvalidFilter;
import dev.willypol.filters.ComparatorFilter;
import dev.willypol.filters.Filter;
import dev.willypol.filters.Filters;
import dev.willypol.operators.ComparatorFilterOperator;
import dev.willypol.order.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.List;

import static dev.willypol.filters.Filter.and;
import static dev.willypol.filters.Filter.closeParenthesis;
import static dev.willypol.filters.Filter.not;
import static dev.willypol.filters.Filter.openParenthesis;
import static dev.willypol.filters.Filter.or;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.Random.class)
class CriteriaToElasticsearchConverterTest {

	@Test
	@DisplayName("Should has private constructor")
	void shouldHasPrivateConstructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		Constructor<CriteriaToElasticsearchConverter> constructor = CriteriaToElasticsearchConverter.class.getDeclaredConstructor();
		assertTrue(Modifier.isPrivate(constructor.getModifiers()));
		constructor.setAccessible(true);
		constructor.newInstance();
	}

	@Test
	@DisplayName("Should create Query from criteria with REG_EXP order by fieldName ASC and pageSize 20")
	void shouldCreateRegexpQueryFromCriteria() throws InvalidFilter, InvalidCriteria {
		// Given
		Filter       f1         = new ComparatorFilter("fieldName", ComparatorFilterOperator.REG_EXP, "^Multi.*$");
		List<Filter> expression = List.of(f1);
		Filters      filters    = new Filters(expression);
		Criteria     criteria   = CriteriaMother.create().withFilters(filters).withOrder(Order.asc("fieldName")).withPageSize(20).build();

		// When
		String result = CriteriaToElasticsearchConverter.toQueryString(criteria);
		String expected = """
				{"from":0,"query":{"regexp":{"fieldName":{"value":"^Multi.*$"}}},"size":20,"sort":[{"fieldName":{"order":"asc"}}]}
				""";

		// Then
		assertEquals(expected.trim(), result.trim());
	}

	@Test
	@DisplayName("Should create Query from criteria with EQUAL_STRICT, GTE and LTE order by age desc")
	void shouldCreateQueryFromCriteriaOrderByAgeDesc() throws InvalidFilter, InvalidCriteria {
		// Given
		Filter   f1       = new ComparatorFilter("enabled", ComparatorFilterOperator.EQUAL_STRICT, true);
		Filter   f2       = new ComparatorFilter("age", ComparatorFilterOperator.GTE, 18);
		Filter   f3       = new ComparatorFilter("age", ComparatorFilterOperator.LTE, 64);
		Filters  filters  = new Filters(List.of(f1, and(), not(), openParenthesis(), f2, or(), f3, closeParenthesis()));
		Criteria criteria = CriteriaMother.create().withFilters(filters).withOrder(Order.desc("age")).build();

		// When
		String result = CriteriaToElasticsearchConverter.toQueryString(criteria);
		String expected = """
				{"query":{"bool":{"must":[{"term":{"enabled":{"value":true}}},{"bool":{"must_not":[{"bool":{"should":[{"range":{"age":{"gte":18}}},{"range":{"age":{"lte":64}}}]}}]}}]}},"sort":[{"age":{"order":"desc"}}]}
				""";

		// Then
		Assertions.assertEquals(expected.trim(), result.trim());
	}


	@Test
	@DisplayName("Should create Query from criteria with NOT_EQUAL_STRICT, LT y GT")
	void shouldCreateBoolQueryFromCriteria() throws InvalidFilter, InvalidCriteria {
		// Given
		Filter   f1       = new ComparatorFilter("enabled", ComparatorFilterOperator.NOT_EQUAL_STRICT, true);
		Filter   f2       = new ComparatorFilter("age", ComparatorFilterOperator.GT, 18);
		Filter   f3       = new ComparatorFilter("age", ComparatorFilterOperator.LT, 65);
		Filters  filters  = new Filters(List.of(f1, or(), openParenthesis(), f2, and(), f3, closeParenthesis()));
		Criteria criteria = CriteriaMother.create().withFilters(filters).build();

		// When
		String result = CriteriaToElasticsearchConverter.toQueryString(criteria);
		String expected = """
				{"query":{"bool":{"should":[{"bool":{"must_not":[{"term":{"enabled":{"value":true}}}]}},{"bool":{"must":[{"range":{"age":{"gt":18}}},{"range":{"age":{"lt":65}}}]}}]}}}
				""";

		// Then
		Assertions.assertEquals(expected.trim(), result.trim());
	}

	@Test
	@DisplayName("Should create  Query from criteria with NOT_EQUAL_STRICT and all types")
	void shouldCreateQueryFromCriteriaNotEqualAndAllTypes() throws InvalidFilter, InvalidCriteria {
		// Given
		Filter f1 = new ComparatorFilter("name", ComparatorFilterOperator.NOT_EQUAL_STRICT, "name");
		Filter f2 = new ComparatorFilter("age", ComparatorFilterOperator.NOT_EQUAL_STRICT, 50);
		Filter f3 = new ComparatorFilter("other", ComparatorFilterOperator.NOT_EQUAL_STRICT, 100L);
		Filter f4 = new ComparatorFilter("andOther", ComparatorFilterOperator.NOT_EQUAL_STRICT, 100L);
		Filter f5 = new ComparatorFilter("enabled", ComparatorFilterOperator.NOT_EQUAL_STRICT, true);

		Filters  filters  = new Filters(List.of(f1, and(), openParenthesis(), f2, and(), f3, and(), f4, or(), f5, closeParenthesis()));
		Criteria criteria = CriteriaMother.create().withFilters(filters).build();

		// When
		String result = CriteriaToElasticsearchConverter.toQueryString(criteria);
		String expected = """
				{"query":{"bool":{"must":[{"bool":{"must_not":[{"term":{"name":{"value":"name"}}}]}},{"bool":{"should":[{"bool":{"must":[{"bool":{"must_not":[{"term":{"age":{"value":50}}}]}},{"bool":{"must_not":[{"term":{"other":{"value":100}}}]}},{"bool":{"must_not":[{"term":{"andOther":{"value":100}}}]}}]}},{"bool":{"must_not":[{"term":{"enabled":{"value":true}}}]}}]}}]}}}
				""";

		// Then
		Assertions.assertEquals(expected.trim(), result.trim());
	}

	@Test
	@DisplayName("Should create Query from criteria with CONTAINS_STRICT and REG_EXP from 10 with size of 10")
	void shouldCreateQueryWithContainsStrictAndRegexpFrom10WithSizeOf10() throws InvalidFilter, InvalidCriteria {
		// Given
		Filter   f1       = new ComparatorFilter("name", ComparatorFilterOperator.CONTAINS_STRICT, "name");
		Filter   f2       = new ComparatorFilter("name", ComparatorFilterOperator.REG_EXP, "ab.+");
		Filters  filters  = new Filters(List.of(f1, and(), f2));
		Criteria criteria = CriteriaMother.create().withFilters(filters).withPageNumber(1).withPageSize(10).build();

		// When
		String result = CriteriaToElasticsearchConverter.toQueryString(criteria);
		String expected = """
				{"from":10,"query":{"bool":{"must":[{"wildcard":{"name":{"value":"*name*"}}},{"regexp":{"name":{"value":"ab.+"}}}]}},"size":10}
				""";

		// Then
		Assertions.assertEquals(expected.trim(), result.trim());
	}

	@Test
	@DisplayName("Should create Query from criteria with NOT_EQUAL, NOT_EQUAL and EQUAL")
	void shouldCreateQueryWithNotEqualContainsAndEqual() throws InvalidFilter, InvalidCriteria {
		// Given
		Filter   f1       = new ComparatorFilter("name", ComparatorFilterOperator.NOT_EQUAL, "name");
		Filter   f2       = new ComparatorFilter("name", ComparatorFilterOperator.CONTAINS, "word");
		Filter   f3       = new ComparatorFilter("name", ComparatorFilterOperator.EQUAL, "word");
		Filters  filters  = new Filters(List.of(f1, and(), f2, and(), f3));
		Criteria criteria = CriteriaMother.create().withFilters(filters).withPageNumber(1).withPageSize(10).build();

		// When
		String result = CriteriaToElasticsearchConverter.toQueryString(criteria);
		String expected = """
				{"from":10,"query":{"bool":{"must":[{"bool":{"must_not":[{"term":{"name":{"value":"name","case_insensitive":true}}}]}},{"wildcard":{"name":{"case_insensitive":true,"value":"*word*"}}},{"term":{"name":{"value":"word","case_insensitive":true}}}]}},"size":10}
				""";

		// Then
		Assertions.assertEquals(expected.trim(), result.trim());
	}


}
