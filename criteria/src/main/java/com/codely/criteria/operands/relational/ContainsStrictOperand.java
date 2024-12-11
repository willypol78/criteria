package com.codely.criteria.operands.relational;

import com.codely.criteria.operands.FilterOperand;
import com.codely.criteria.operators.ComparatorFilterOperator;
import com.codely.criteria.operators.FilterOperator;
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
