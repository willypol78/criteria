package dev.willypol.operands;

import dev.willypol.filters.Filter;

import java.util.List;

/**
 * Interface representing a filter operand that can contain other operands.
 * <p>
 * This interface is used to define a structure for filter operands that can
 * </p>
 *
 * @author Guillermo Mir
 * @since 1.0.0
 */
public interface FilterOperand {
	/**
	 * Returns the list of child operands for this filter operand.
	 *
	 * @return a list of child operands, or an empty list if there are none
	 */
	List<FilterOperand> operands();
}
