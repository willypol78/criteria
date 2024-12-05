package tv.codely.criteria.operands.relational;

import tv.codely.criteria.operands.FilterOperand;
import tv.codely.criteria.operators.ComparatorFilterOperator;
import tv.codely.criteria.operators.FilterOperator;
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
