package dev.willypol.operators;

import java.util.Arrays;
import java.util.Optional;

public enum ParenthesisFilterOperator implements FilterOperator {
	OPEN_PARENTHESIS("("),
	CLOSE_PARENTHESIS(")");

	private final String operator;

	ParenthesisFilterOperator(final String operator) {
		this.operator = operator;
	}

	public static Optional<ParenthesisFilterOperator> fromValue(Object value) {
		return Arrays.stream(ParenthesisFilterOperator.values())
				.filter(f -> f.operator.equalsIgnoreCase(value.toString()))
				.map(Optional::of)
				.findFirst()
				.orElse(Optional.empty());
	}

	public String operator() {
		return operator;
	}
}
