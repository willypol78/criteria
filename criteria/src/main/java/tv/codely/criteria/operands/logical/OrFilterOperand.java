package tv.codely.criteria.operands.logical;

import tv.codely.criteria.operands.FilterOperand;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is a record class that implements the LogicalOperator interface.
 * This class is used to represent the logical operator "OR" in a filter.
 *
 * @param operands The list of operands that are part of the logical operator.
 */
public record OrFilterOperand(List<FilterOperand> operands) implements LogicalFilterOperand {

	/**
	 * Inverts the order of the operands in the specification list.
	 */
	public OrFilterOperand {
		Collections.reverse(operands);
	}

	/**
	 * This method returns a string representation of this operator, in the format "OR(operand1, operand2, ...)".
	 *
	 * @return A String representation of this operator.
	 */
	@Override
	public String toString() {
		return "OR" + operands.stream().map(String::valueOf).collect(Collectors.joining(", ", "(", ")"));
	}
}
