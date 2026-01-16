package io.github.willypol78.criteria.operands.relational;

import io.github.willypol78.criteria.operands.FilterOperand;
import io.github.willypol78.criteria.operators.FilterOperator;


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
