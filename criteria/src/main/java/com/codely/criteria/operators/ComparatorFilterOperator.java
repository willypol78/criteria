package com.codely.criteria.operators;

import java.util.Arrays;
import java.util.Optional;

public enum ComparatorFilterOperator implements FilterOperator {
	EQUAL("="),
	EQUAL_STRICT("=="),
	NOT_EQUAL("!="),
	NOT_EQUAL_STRICT("!=="),
	GT(">"),
	LT("<"),
	GTE(">="),
	LTE("<="),
	CONTAINS("=*"),
	CONTAINS_STRICT("==*"),
	REG_EXP("~");

	private final String operator;

	ComparatorFilterOperator(final String operator) {
		this.operator = operator;
	}

	public static Optional<ComparatorFilterOperator> fromValue(Object value) {
		return Arrays.stream(ComparatorFilterOperator.values())
				.filter(f -> isOperatorInValues(value.toString()) || isOperatorInEnum(value.toString()))
				.map(Optional::of)
				.findFirst()
				.orElse(Optional.empty());
	}

	private static boolean isOperatorInValues(String value) {
		return Arrays.stream(ComparatorFilterOperator.values()).anyMatch(f -> f.operator.equalsIgnoreCase(value));
	}

	public static boolean isOperatorInEnum(String value) {
		try {
			ComparatorFilterOperator.valueOf(value);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	public String operator() {
		return operator;
	}
}
