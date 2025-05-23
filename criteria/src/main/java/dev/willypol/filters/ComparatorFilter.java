package dev.willypol.filters;

import dev.willypol.errors.InvalidFilter;
import dev.willypol.errors.InvalidFilterName;
import dev.willypol.errors.InvalidFilterOperator;
import dev.willypol.errors.InvalidFilterValue;
import dev.willypol.operators.ComparatorFilterOperator;
import dev.willypol.operators.FilterOperator;
import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

public record ComparatorFilter(String field, FilterOperator filterOperator, Serializable value) implements Filter {

	public static ComparatorFilter fromPrimitives(final FilterPrimitives filterPrimitives) throws InvalidFilter {
		String                   field    = filterPrimitives.field().orElseThrow(InvalidFilterName::new);
		Serializable             value    = filterPrimitives.value().orElseThrow(InvalidFilterValue::new);
		ComparatorFilterOperator operator = ComparatorFilterOperator.fromValue(filterPrimitives.operator()).orElseThrow(InvalidFilterOperator::new);

		return new ComparatorFilter(field, operator, value);
	}

	public static ComparatorFilter fromValues(Map<String, Serializable> values) throws InvalidFilterOperator {
		checkValidValues(values);

		Optional<ComparatorFilterOperator> operator = ComparatorFilterOperator.fromValue(values.get("operator"));

		if (operator.isEmpty()) {
			throw new InvalidFilterOperator();
		}

		return new ComparatorFilter(values.get("field").toString(), operator.get(), values.get("value"));
	}

	private static void checkValidValues(final Map<String, Serializable> values) throws InvalidFilterOperator {
		if (values.size() != 3 || !values.containsKey("field") || !values.containsKey("value") || !values.containsKey("operator")) {
			throw new InvalidFilterOperator();
		}
	}

	public ComparatorFilterOperator operator() {
		return (ComparatorFilterOperator) filterOperator;
	}


}
