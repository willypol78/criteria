package dev.willypol.operands.logical;

import dev.willypol.operands.FilterOperand;

public sealed interface LogicalFilterOperand extends FilterOperand permits AndFilterOperand, NotFilterOperand, OrFilterOperand, VoidFilterOperand {}
