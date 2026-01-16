package io.github.willypol78.criteria.filters;

import io.github.willypol78.criteria.errors.InvalidFilter;
import io.github.willypol78.criteria.errors.InvalidFilterName;
import io.github.willypol78.criteria.errors.InvalidFilterOperator;
import io.github.willypol78.criteria.errors.InvalidFilterValue;
import io.github.willypol78.criteria.operators.ComparatorFilterOperator;
import io.github.willypol78.criteria.operators.FilterOperator;
import java.io.Serializable;
import java.util.Map;
import java.util.Optional;


/**
 * Represents a filter that compares a field with a value using a specific operator.
 * <p>
 * This filter can be used to apply conditions on fields in a data set, such as checking
 * if a field is equal to, greater than, less than, etc. a specified value.
 * </p>
 *
 * @param field          the name of the field to compare
 * @param filterOperator the operator to use for comparison
 * @param value          the value to compare against
 * @author Guillermo Mir
 * @since 1.0.0
 */
public record ComparatorFilter(String field, FilterOperator filterOperator, Serializable value) implements Filter {

	/**
	 * Creates a {@code ComparatorFilter} from a map of values.
	 *
	 * @param filterPrimitives a FilterPrimitives
	 * @return a new instance of {@code ComparatorFilter}
	 * @throws InvalidFilterOperator if the operator is invalid or required keys are missing
	 */
	public static ComparatorFilter fromPrimitives(final FilterPrimitives filterPrimitives) throws InvalidFilter {
		String                   field    = filterPrimitives.field().orElseThrow(InvalidFilterName::new);
		Serializable             value    = filterPrimitives.value().orElseThrow(InvalidFilterValue::new);
		ComparatorFilterOperator operator = ComparatorFilterOperator.fromValue(filterPrimitives.operator()).orElseThrow(InvalidFilterOperator::new);

		return new ComparatorFilter(field, operator, value);
	}

	/**
	 * Returns the specific comparator operator for this filter.
	 *
	 * @param values a map containing the filter values, must include "field", "operator", and "value" keys
	 * @return the comparator filter operator
	 * @throws InvalidFilterOperator if the operator is invalid or required keys are missing
	 */
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


	/**
	 * Retrieves the comparator operator associated with this filter.
	 *
	 * @return the comparator filter operator used for comparison
	 */
	public ComparatorFilterOperator operator() {
		return (ComparatorFilterOperator) filterOperator;
	}


}
