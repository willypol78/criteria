package tv.codely.criteria;

import tv.codely.criteria.errors.InvalidFilter;
import tv.codely.criteria.filters.AndFilter;
import tv.codely.criteria.filters.CloseParenthesisFilter;
import tv.codely.criteria.filters.ComparatorFilter;
import tv.codely.criteria.filters.Filter;
import tv.codely.criteria.filters.NotFilter;
import tv.codely.criteria.filters.OpenParenthesisFilter;
import tv.codely.criteria.filters.OrFilter;
import tv.codely.criteria.operands.FilterOperand;
import tv.codely.criteria.operands.logical.AndFilterOperand;
import tv.codely.criteria.operands.logical.NotFilterOperand;
import tv.codely.criteria.operands.logical.OrFilterOperand;
import tv.codely.criteria.operands.logical.VoidFilterOperand;
import tv.codely.criteria.operands.relational.ContainsOperand;
import tv.codely.criteria.operands.relational.ContainsStrictOperand;
import tv.codely.criteria.operands.relational.EqualsOperand;
import tv.codely.criteria.operands.relational.EqualsStrictOperand;
import tv.codely.criteria.operands.relational.GreaterThanEqualToOperand;
import tv.codely.criteria.operands.relational.GreaterThanOperand;
import tv.codely.criteria.operands.relational.LowerThanEqualOperand;
import tv.codely.criteria.operands.relational.LowerThanOperand;
import tv.codely.criteria.operands.relational.NotEqualsOperand;
import tv.codely.criteria.operands.relational.NotEqualsStrictOperand;
import tv.codely.criteria.operands.relational.RegExpOperand;
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
