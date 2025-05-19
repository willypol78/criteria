package dev.willypol.operators;

import java.util.Optional;

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

	String operator();
}
