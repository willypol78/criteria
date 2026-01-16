package io.github.willypol78.criteria.operators;

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
        String valueStr = value.toString();
        return Arrays.stream(ComparatorFilterOperator.values())
                .filter(f -> f.operator.equalsIgnoreCase(valueStr) || f.name().equalsIgnoreCase(valueStr))
                .findFirst();
    }

    public String operator() {
        return operator;
    }
}
