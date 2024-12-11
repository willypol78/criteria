package com.codely.criteria.operators;

import java.util.Arrays;
import java.util.Optional;

public enum BooleanFilterOperator implements FilterOperator {
	NOT("NOT"),
	AND("AND"),
	OR("OR");

	private final String operator;

	BooleanFilterOperator(final String operator) {
		this.operator = operator;
	}

	public static Optional<BooleanFilterOperator> fromValue(Object value) {
		return Arrays.stream(BooleanFilterOperator.values())
				.filter(f -> f.operator.equalsIgnoreCase(value.toString()))
				.map(Optional::of)
				.findFirst()
				.orElse(Optional.empty());
	}

	public String operator() {
		return operator;
	}
}
