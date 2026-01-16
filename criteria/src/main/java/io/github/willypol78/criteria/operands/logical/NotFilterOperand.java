package io.github.willypol78.criteria.operands.logical;

import io.github.willypol78.criteria.operands.FilterOperand;
import java.util.List;

/**
 * This is a final class that implements the LogicalOperator interface.
 * This class is used to represent the logical operand "NOT" in a filter.
 */
public final class NotFilterOperand implements LogicalFilterOperand {

	private final FilterOperand filterOperand;

	/**
	 * Initializes the operator that must be negated.
	 *
	 * @param filterOperand The operand that must be negated.
	 */
	public NotFilterOperand(FilterOperand filterOperand) {
		this.filterOperand = filterOperand;
	}

	/**
	 * This method returns a list of operator specifications containing only this operator.
	 *
	 * @return A list of operator specifications containing only this operator.
	 */
	@Override
	public List<FilterOperand> operands() {
		return List.of(filterOperand);
	}

	/**
	 * This method returns a string representation of this operator, in the format "NOT(operator)".
	 *
	 * @return The string representation of this operator.
	 */
	@Override
	public String toString() {
		return "NOT(" + filterOperand + ")";
	}
}
