package io.github.willypol78.criteria.operators;

import java.util.Optional;


/**
 * A sealed interface that represents a filter operator used for constructing filter expressions.
 * This interface is implemented by specific operator types such as boolean operators, comparator operators,
 * and parenthesis operators.
 * The known implementations of this interface are:
 * - {@code BooleanFilterOperator}: Represents logical operators such as AND, OR, and NOT.
 * - {@code ComparatorFilterOperator}: Represents comparison operators such as EQUAL, GT, LT, etc.
 * - {@code ParenthesisFilterOperator}: Represents parenthesis operators used for grouping conditions.
 */
public sealed interface FilterOperator permits BooleanFilterOperator, ComparatorFilterOperator, ParenthesisFilterOperator {

	static Optional<FilterOperator> fromValue(String operator) {
		if (operator == null) {
			return Optional.empty();
		}
		return switch (operator) {
			case "(" -> Optional.of(ParenthesisFilterOperator.OPEN_PARENTHESIS);
			case ")" -> Optional.of(ParenthesisFilterOperator.CLOSE_PARENTHESIS);
			case "NOT" -> Optional.of(BooleanFilterOperator.NOT);
			case "AND" -> Optional.of(BooleanFilterOperator.AND);
			case "OR" -> Optional.of(BooleanFilterOperator.OR);
			default -> ComparatorFilterOperator.fromValue(operator).map(op -> (FilterOperator) op);
		};
	}

	/**
	 * Returns the string representation of this filter operator.
	 *
	 * @return the operator as a string
	 */
	String operator();
}
