package dev.willypol.operands.logical;

import dev.willypol.operands.FilterOperand;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is a record class that implements the LogicalFilterOperand interface.
 * This class is used to represent the logical operand "AND" in a filter.
 *
 * @param operands List of operands that are part of the AND.
 */
public record AndFilterOperand(List<FilterOperand> operands) implements LogicalFilterOperand {

	/**
	 * Inverts the order of the operands in the specification list.
	 */
	public AndFilterOperand {
		Collections.reverse(operands);
	}

	/**
	 * This method returns a string representation of this operand, in the format "AND(operand1, operand2, ...)".
	 *
	 * @return String representation of this operand.
	 */
	@Override
	public String toString() {
		return "AND" + operands.stream().map(String::valueOf).collect(Collectors.joining(", ", "(", ")"));
	}
}
