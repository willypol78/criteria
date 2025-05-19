package dev.willypol.operands.relational;

import dev.willypol.operands.FilterOperand;
import dev.willypol.operators.FilterOperator;


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
