package tv.codely.criteria.operands.relational;

import tv.codely.criteria.operands.FilterOperand;
import tv.codely.criteria.operators.ComparatorFilterOperator;
import tv.codely.criteria.operators.FilterOperator;
import java.util.List;


public record NotEqualsOperand(String field, Object value) implements RelationalOperand {

	@Override
	public FilterOperator operator() {
		return ComparatorFilterOperator.NOT_EQUAL;
	}

	@Override
	public List<FilterOperand> operands() {
		return List.of(this);
	}

	@Override
	public String toString() {
		return "UPPER(" + field + ")!=UPPER(" + value + ")";
	}
}
