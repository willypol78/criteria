package dev.willypol;

import dev.willypol.errors.InvalidFilter;
import dev.willypol.filters.AndFilter;
import dev.willypol.filters.CloseParenthesisFilter;
import dev.willypol.filters.ComparatorFilter;
import dev.willypol.filters.Filter;
import dev.willypol.filters.NotFilter;
import dev.willypol.filters.OpenParenthesisFilter;
import dev.willypol.filters.OrFilter;
import dev.willypol.operands.FilterOperand;
import dev.willypol.operands.logical.AndFilterOperand;
import dev.willypol.operands.logical.NotFilterOperand;
import dev.willypol.operands.logical.OrFilterOperand;
import dev.willypol.operands.logical.VoidFilterOperand;
import dev.willypol.operands.relational.ContainsOperand;
import dev.willypol.operands.relational.ContainsStrictOperand;
import dev.willypol.operands.relational.EqualsOperand;
import dev.willypol.operands.relational.EqualsStrictOperand;
import dev.willypol.operands.relational.GreaterThanEqualToOperand;
import dev.willypol.operands.relational.GreaterThanOperand;
import dev.willypol.operands.relational.LowerThanEqualOperand;
import dev.willypol.operands.relational.LowerThanOperand;
import dev.willypol.operands.relational.NotEqualsOperand;
import dev.willypol.operands.relational.NotEqualsStrictOperand;
import dev.willypol.operands.relational.RegExpOperand;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public final class CriteriaToFilterOperandConverter {

	public FilterOperand convert(Criteria criteria) throws InvalidFilter {
		if (criteria.filterSize() == 0) {
			return new VoidFilterOperand();
		}
		Queue<Filter> filterQueue = new ArrayDeque<>(criteria.filters());
		return convert(filterQueue).pop();
	}

	private Deque<FilterOperand> convert(Queue<Filter> filters) throws InvalidFilter {
		Deque<FilterOperand> operands  = new ArrayDeque<>();
		Deque<Filter>        operators = new ArrayDeque<>();

		while (!filters.isEmpty()) {
			Filter filter = filters.poll();
			switch (filter) {
				case CloseParenthesisFilter f -> {
					// No action needed
				}
				case OpenParenthesisFilter f -> pushOperand(operands, operators, convert(filters));
				case ComparatorFilter f -> pushOperand(operands, operators, new ArrayDeque<>(List.of(operator(f))));
				case AndFilter f -> pushOperatorAndCalculateOperands(operands, operators, f);
				case OrFilter f -> pushOperatorAndCalculateOperands(operands, operators, f);
				case NotFilter f -> pushOperatorAndCalculateOperands(operands, operators, f);
				default -> throw new InvalidFilter("Filter not recognized");
			}
		}
		while (!operators.isEmpty()) {
			extractOperands(operands, operators);
		}
		return operands;
	}

	private void pushOperand(final Deque<FilterOperand> operands, final Deque<Filter> operators, final Deque<FilterOperand> filterOperands) {
		if (!operators.isEmpty() && operators.peek() instanceof NotFilter) {
			operators.pop();
			operands.push(new NotFilterOperand(filterOperands.pop()));
		} else {
			pushOperand(operands, filterOperands.pop());
		}
	}

	private void extractOperands(final Deque<FilterOperand> operands, final Deque<Filter> operators) {
		Filter              filter = operators.pop();
		List<FilterOperand> ops    = new LinkedList<>();
		ops.add(operands.pop());
		ops.add(operands.pop());
		while (!operators.isEmpty() && filter.getClass().equals(operators.peek().getClass())) { // check if the filter is the same type
			operators.pop();
			ops.add(operands.pop());
		}
		if (filter instanceof OrFilter) {
			operands.push(new OrFilterOperand(ops));
		} else {
			operands.push(new AndFilterOperand(ops));
		}
	}

	private void pushOperand(final Deque<FilterOperand> operands, FilterOperand spec) {
		operands.push(spec);
	}

	private void pushOperatorAndCalculateOperands(final Deque<FilterOperand> operands, final Deque<Filter> operators, final Filter filter) {
		if (!operators.isEmpty() && filter instanceof OrFilter) {
			Filter previous = operators.pop();
			if (previous instanceof OrFilter) {
				operators.push(previous);
				operators.push(filter);
			} else {
				operators.push(previous);
				extractOperands(operands, operators);
				operators.push(filter);
			}
		} else {
			operators.push(filter);
		}
	}

	private FilterOperand operator(ComparatorFilter filter) {
		String field = filter.field();
		var    value = filter.value();
		return switch (filter.operator()) {
			case EQUAL -> new EqualsOperand(field, value);
			case EQUAL_STRICT -> new EqualsStrictOperand(field, value);
			case NOT_EQUAL -> new NotEqualsOperand(field, value);
			case NOT_EQUAL_STRICT -> new NotEqualsStrictOperand(field, value);
			case GT -> new GreaterThanOperand(field, value);
			case LT -> new LowerThanOperand(field, value);
			case GTE -> new GreaterThanEqualToOperand(field, value);
			case LTE -> new LowerThanEqualOperand(field, value);
			case CONTAINS -> new ContainsOperand(field, value);
			case CONTAINS_STRICT -> new ContainsStrictOperand(field, value);
			case REG_EXP -> new RegExpOperand(field, value);
		};
	}
}
