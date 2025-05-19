package dev.willypol.operands.relational;

import dev.willypol.operands.FilterOperand;
import dev.willypol.operators.ComparatorFilterOperator;
import dev.willypol.operators.FilterOperator;
import java.util.List;


public record GreaterThanEqualToOperand(String field, Object value) implements RelationalOperand {

	@Override
	public FilterOperator operator() {
		return ComparatorFilterOperator.GTE;
	}

	@Override
	public List<FilterOperand> operands() {
		return List.of(this);
	}

	@Override
	public String toString() {
		return field + ">=" + value;
	}
}
