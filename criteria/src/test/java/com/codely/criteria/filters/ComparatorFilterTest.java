package com.codely.criteria.filters;

import com.codely.criteria.errors.InvalidFilterOperator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.Serializable;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ComparatorFilterTest {


	@Test
	@DisplayName("Should throw invalid filter operator if not is valid operator")
	void shouldThrowInvalidFilterOperatorIfNotIsValidOperator() {
		Map<String, Serializable> values = Map.of("field", "fieldValue", "operator", "InvalidOperator", "value", "fieldValue");
		assertThrows(InvalidFilterOperator.class, () -> ComparatorFilter.fromValues(values));
	}
}
