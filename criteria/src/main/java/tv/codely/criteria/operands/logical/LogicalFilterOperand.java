package tv.codely.criteria.operands.logical;

import tv.codely.criteria.operands.FilterOperand;

public sealed interface LogicalFilterOperand extends FilterOperand permits AndFilterOperand, NotFilterOperand, OrFilterOperand, VoidFilterOperand {}
