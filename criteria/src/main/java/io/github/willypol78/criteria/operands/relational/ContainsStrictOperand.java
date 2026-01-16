package io.github.willypol78.criteria.operands.relational;

import io.github.willypol78.criteria.operands.FilterOperand;
import io.github.willypol78.criteria.operators.ComparatorFilterOperator;
import io.github.willypol78.criteria.operators.FilterOperator;
import java.io.Serializable;
import java.util.List;

public record ContainsStrictOperand(String field, Object value) implements RelationalOperand, Serializable {

	@Override
	public FilterOperator operator() {
		return ComparatorFilterOperator.CONTAINS_STRICT;
	}

	@Override
	public List<FilterOperand> operands() {
		return List.of(this);
	}

	@Override
	public String toString() {
		return field + " LIKE '%" + value + "%'";
	}
}
