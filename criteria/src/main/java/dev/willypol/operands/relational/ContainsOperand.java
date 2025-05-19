package dev.willypol.operands.relational;

import dev.willypol.operands.FilterOperand;
import dev.willypol.operators.ComparatorFilterOperator;
import dev.willypol.operators.FilterOperator;
import java.io.Serializable;
import java.util.List;


public record ContainsOperand(String field, Object value) implements RelationalOperand, Serializable {


	@Override
	public FilterOperator operator() {
		return ComparatorFilterOperator.CONTAINS;
	}

	@Override
	public List<FilterOperand> operands() {
		return List.of(this);
	}

	@Override
	public String toString() {
		return "UPPER(" + field + ") LIKE '%UPPER(" + value + ")%'";
	}
}
