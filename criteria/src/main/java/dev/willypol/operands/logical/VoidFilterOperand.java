package dev.willypol.operands.logical;

import dev.willypol.operands.FilterOperand;
import java.util.Collections;
import java.util.List;

/**
 * This record class implements the LogicalOperand interface.
 * This class is used to represent a "VOID" logical operand in a filter.
 * The VOID operand has no specifications and is used to represent an empty or null state.
 */
public record VoidFilterOperand() implements LogicalFilterOperand {

	/**
	 * This method returns an empty list of operand specifications.
	 * As a VOID operand has no specifications, this method always returns an empty list.
	 *
	 * @return An empty list of operand specifications.
	 */
	@Override
	public List<FilterOperand> operands() {
		return Collections.emptyList();
	}
}
