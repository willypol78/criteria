package tv.codely.criteria.operands.relational;

import tv.codely.criteria.operands.FilterOperand;
import tv.codely.criteria.operators.ComparatorFilterOperator;
import tv.codely.criteria.operators.FilterOperator;
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
