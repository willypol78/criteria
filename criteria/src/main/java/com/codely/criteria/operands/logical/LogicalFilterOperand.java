package com.codely.criteria.operands.logical;

import com.codely.criteria.operands.FilterOperand;

public sealed interface LogicalFilterOperand extends FilterOperand permits AndFilterOperand, NotFilterOperand, OrFilterOperand, VoidFilterOperand {}
