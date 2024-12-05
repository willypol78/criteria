package tv.codely.criteria.filters;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tv.codely.criteria.errors.InvalidFilterOperator;
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
