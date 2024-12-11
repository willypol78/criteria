package com.codely.criteria.operands.relational;

import com.codely.criteria.operands.FilterOperand;
import com.codely.criteria.operators.FilterOperator;


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
