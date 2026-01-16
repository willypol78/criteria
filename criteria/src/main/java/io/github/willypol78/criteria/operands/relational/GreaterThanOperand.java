package io.github.willypol78.criteria.operands.relational;

import io.github.willypol78.criteria.operands.FilterOperand;
import io.github.willypol78.criteria.operators.ComparatorFilterOperator;
import io.github.willypol78.criteria.operators.FilterOperator;
import java.util.List;


public record GreaterThanOperand(String field, Object value) implements RelationalOperand {

	@Override
	public FilterOperator operator() {
		return ComparatorFilterOperator.GT;
	}

	@Override
	public List<FilterOperand> operands() {
		return List.of(this);
	}

	@Override
	public String toString() {
		return field + ">" + value;
	}
}
