package io.github.willypol78.criteria.to;

import io.github.willypol78.criteria.Criteria;
import io.github.willypol78.criteria.CriteriaMother;
import io.github.willypol78.criteria.errors.InvalidCriteria;
import io.github.willypol78.criteria.errors.InvalidFilter;
import io.github.willypol78.criteria.filters.ComparatorFilter;
import io.github.willypol78.criteria.filters.Filter;
import io.github.willypol78.criteria.filters.Filters;
import io.github.willypol78.criteria.operators.ComparatorFilterOperator;
import io.github.willypol78.criteria.order.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static io.github.willypol78.criteria.filters.Filter.and;
import static io.github.willypol78.criteria.filters.Filter.closeParenthesis;
import static io.github.willypol78.criteria.filters.Filter.openParenthesis;
import static io.github.willypol78.criteria.filters.Filter.or;
import static org.junit.jupiter.api.Assertions.assertEquals;


class CriteriaToSqlConverterTest {

	static Stream<SQLTransformer> transformerProvider() {
		return Stream.of(new MySQLTransformer(), new PostgreSQLTransformer());
	}

	@ParameterizedTest
	@MethodSource("transformerProvider")
	@DisplayName("Should create Query from criteria with CONTAINS,EQUAL_STRICT,NOT_EQUAL and REG_EXP  order by fieldName ASC and pageSize 20")
	void shouldCreateSQLWithOrderAndPageSizeFromCriteria(SQLTransformer transformer) throws InvalidFilter, InvalidCriteria {
		// Given
		Filter       f1         = new ComparatorFilter("fieldName1", ComparatorFilterOperator.CONTAINS, "pepe");
		Filter       f2         = new ComparatorFilter("fieldName2", ComparatorFilterOperator.EQUAL_STRICT, "juan");
		Filter       f3         = new ComparatorFilter("fieldName3", ComparatorFilterOperator.NOT_EQUAL, "luis");
		Filter       f4         = new ComparatorFilter("fieldName4", ComparatorFilterOperator.REG_EXP, "pe.+");
		List<Filter> expression = List.of(f1, or(), openParenthesis(), f2, and(), f3, or(), f4, closeParenthesis());
		Filters      filters    = new Filters(expression);
		Criteria     criteria   = CriteriaMother.create().withFilters(filters).withOrder(Order.asc("fieldName")).withPageSize(20).build();

		// When
		SqlAndParams result = new CriteriaToSqlConverter(transformer).convert("testTable", criteria);
		String expectedSql = """
				SELECT * FROM testTable WHERE 1=1 AND fieldName1 LIKE *:fieldName1_0* OR (fieldName2 = :fieldName2_1 AND fieldName3 != :fieldName3_2 OR fieldName4 ~ :fieldName4_3) ORDER BY fieldName asc LIMIT 20
				""";
		Map<String, Serializable> expectedParams = Map.of(":fieldName1_0", "pepe"
				, ":fieldName2_1", "juan"
				, ":fieldName3_2", "luis"
				, ":fieldName4_3", "pe.+");

		// Then
		assertEquals(expectedSql.trim(), result.sql());
		assertEquals(expectedParams, result.params());
	}

	@Test
	@DisplayName("Should create PostgreSQL SQL from criteria with CONTAINS,EQUAL_STRICT,NOT_EQUAL and REG_EXP  order by fieldName ASC and pageSize 20")
	void shouldCreatePostgreSQLWithPageSizeAndPageNumberFromCriteria() throws InvalidFilter, InvalidCriteria {
		// Given
		Filter       f1         = new ComparatorFilter("fieldName1", ComparatorFilterOperator.LT, 100.0);
		Filter       f2         = new ComparatorFilter("fieldName2", ComparatorFilterOperator.GT, 20);
		Filter       f3         = new ComparatorFilter("fieldName3", ComparatorFilterOperator.LTE, 20L);
		Filter       f4         = new ComparatorFilter("fieldName4", ComparatorFilterOperator.GTE, 50);
		List<Filter> expression = List.of(f1, and(), f2, and(), openParenthesis(), f3, or(), f4, closeParenthesis());
		Filters      filters    = new Filters(expression);
		Criteria     criteria   = CriteriaMother.create().withFilters(filters).withPageSize(20).withPageNumber(2).build();

		// When
		SqlAndParams result = new CriteriaToSqlConverter(new PostgreSQLTransformer()).convert("testTable", criteria);
		String expectedSql = """
				SELECT * FROM testTable WHERE 1=1 AND fieldName1 < :fieldName1_0 AND fieldName2 > :fieldName2_1 AND (fieldName3 <= :fieldName3_2 OR fieldName4 >= :fieldName4_3) LIMIT 20 OFFSET 40
				""";
		Map<String, Serializable> expectedParams = Map.of(":fieldName1_0", 100.0
				, ":fieldName2_1", 20
				, ":fieldName3_2", 20L
				, ":fieldName4_3", 50);

		// Then
		assertEquals(expectedSql.trim(), result.sql());
		assertEquals(expectedParams, result.params());
	}

	@Test
	@DisplayName("Should create MySQL SQL from criteria with CONTAINS,EQUAL_STRICT,NOT_EQUAL and REG_EXP  order by fieldName ASC and pageSize 20")
	void shouldCreateMySQLWithPageSizeAndPageNumberFromCriteria() throws InvalidFilter, InvalidCriteria {
		// Given
		Filter       f1         = new ComparatorFilter("fieldName1", ComparatorFilterOperator.LT, 100.0);
		Filter       f2         = new ComparatorFilter("fieldName2", ComparatorFilterOperator.GT, 20);
		Filter       f3         = new ComparatorFilter("fieldName3", ComparatorFilterOperator.LTE, 20L);
		Filter       f4         = new ComparatorFilter("fieldName4", ComparatorFilterOperator.GTE, 50);
		List<Filter> expression = List.of(f1, and(), f2, and(), openParenthesis(), f3, or(), f4, closeParenthesis());
		Filters      filters    = new Filters(expression);
		Criteria     criteria   = CriteriaMother.create().withFilters(filters).withPageSize(20).withPageNumber(2).build();

		// When
		SqlAndParams result = new CriteriaToSqlConverter(new MySQLTransformer()).convert("testTable", criteria);
		String expectedSql = """
				SELECT * FROM testTable WHERE 1=1 AND fieldName1 < :fieldName1_0 AND fieldName2 > :fieldName2_1 AND (fieldName3 <= :fieldName3_2 OR fieldName4 >= :fieldName4_3) LIMIT 20, 40
				""";
		Map<String, Serializable> expectedParams = Map.of(":fieldName1_0", 100.0
				, ":fieldName2_1", 20
				, ":fieldName3_2", 20L
				, ":fieldName4_3", 50);

		// Then
		assertEquals(expectedSql.trim(), result.sql());
		assertEquals(expectedParams, result.params());
	}
	
}
