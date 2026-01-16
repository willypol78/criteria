package io.github.willypol78.criteria.operands.logical;

import io.github.willypol78.criteria.operands.FilterOperand;

public sealed interface LogicalFilterOperand extends FilterOperand permits AndFilterOperand, NotFilterOperand, OrFilterOperand, VoidFilterOperand {}
