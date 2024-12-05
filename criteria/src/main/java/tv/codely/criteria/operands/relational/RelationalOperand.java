package tv.codely.criteria.operands.relational;

import tv.codely.criteria.operands.FilterOperand;
import tv.codely.criteria.operators.FilterOperator;


public sealed interface RelationalOperand extends FilterOperand permits
		EqualsStrictOperand,
		EqualsOperand,
		RegExpOperand,
		GreaterThanEqualToOperand,
		LowerThanEqualOperand,
		GreaterThanOperand,
		LowerThanOperand,
		ContainsStrictOperand,
		ContainsOperand,
		NotEqualsOperand,
		NotEqualsStrictOperand {

	String field();
	Object value();
	FilterOperator operator();
}
