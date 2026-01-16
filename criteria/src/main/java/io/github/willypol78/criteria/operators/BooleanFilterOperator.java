package io.github.willypol78.criteria.operators;

import java.util.Arrays;
import java.util.Optional;

/**
 * Enum representing boolean filter operators used for combining or negating filter conditions.
 * Supported operators are NOT, AND, and OR.
 *
 * @author Guillermo Mir
 * @since 1.0.0
 */
public enum BooleanFilterOperator implements FilterOperator {
	/**
	 * Logical NOT operator.
	 */
	NOT("NOT"),
	/**
	 * Logical AND operator.
	 */
	AND("AND"),
	/**
	 * Logical OR operator.
	 */
	OR("OR");


	private final String operator;


	/**
	 * Constructs a BooleanFilterOperator with the specified operator name.
	 *
	 * @param operator the string representation of the operator
	 */

	BooleanFilterOperator(final String operator) {
		this.operator = operator;
	}

	/**
	 * Returns an {@link Optional} containing the {@code BooleanFilterOperator} that matches the given value,
	 * or an empty {@code Optional} if no match is found.
	 *
	 * @param value the value to match against the operator names
	 * @return an {@code Optional} containing the matching operator, or empty if not found
	 */

	public static Optional<BooleanFilterOperator> fromValue(Object value) {
		return Arrays.stream(BooleanFilterOperator.values())
				.filter(f -> f.operator.equalsIgnoreCase(value.toString()))
				.map(Optional::of)
				.findFirst()
				.orElse(Optional.empty());
	}


	/**
	 * Returns the string representation of this operator.
	 *
	 * @return the operator as a string
	 */
	public String operator() {
		return operator;
	}
}
