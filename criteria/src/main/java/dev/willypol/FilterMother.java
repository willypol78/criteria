package dev.willypol;

import dev.willypol.filters.AndFilter;
import dev.willypol.filters.CloseParenthesisFilter;
import dev.willypol.filters.ComparatorFilter;
import dev.willypol.filters.Filter;
import dev.willypol.filters.Filters;
import dev.willypol.filters.NotFilter;
import dev.willypol.filters.OpenParenthesisFilter;
import dev.willypol.filters.OrFilter;
import dev.willypol.operators.ComparatorFilterOperator;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class FilterMother {

	private final Random random = new Random();

	List<Filter> filters = new ArrayList<>();

	// AndFilter, CloseParenthesisFilter, ComparatorFilter, NotFilter, OpenParenthesisFilter, OrFilter
	private FilterMother() {}

	public static FilterMother create() {
		return new FilterMother();
	}

	public FilterMother and() {
		filters.add(new AndFilter());
		return this;
	}

	public FilterMother or() {
		filters.add(new OrFilter());
		return this;
	}

	public FilterMother openParenthesis() {
		filters.add(new OpenParenthesisFilter());
		return this;
	}

	public FilterMother closeParenthesis() {
		filters.add(new CloseParenthesisFilter());
		return this;
	}

	public FilterMother not() {
		filters.add(new NotFilter());
		return this;
	}

	public FilterMother comparator() {
		int                      randomNumber = random.nextInt(5);
		int                      operatorIdx  = random.nextInt(ComparatorFilterOperator.values().length);
		ComparatorFilterOperator randOp       = ComparatorFilterOperator.values()[operatorIdx];
		if (operatorIdx >= 4 && operatorIdx <= 7) { // GT GTE LT LTE
			filters.add(new ComparatorFilter("field" + randomNumber, randOp, randomNumber));
		} else {
			filters.add(new ComparatorFilter("field" + randomNumber, randOp, "value" + randomNumber));
		}
		return this;
	}

	public FilterMother random(int total) {

		random.nextInt(5);
		comparator();
		for (int i = 0; i < total; i++) {
			int randomOperator = random.nextInt(5);
			switch (randomOperator) {
				case 0 -> {
					and();
					comparator();
				}
				case 1 -> {
					or();
					comparator();
				}
				case 2 -> {
					and();
					openParenthesis();
					comparator();
					and();
					comparator();
					closeParenthesis();
				}
				case 3 -> {
					and();
					openParenthesis();
					comparator();
					or();
					comparator();
					closeParenthesis();
				}
				case 4 -> {
					not();
					comparator();
				}
				default -> {
					// do nothing
				}
			}
		}
		return this;
	}

	public Filters build() {
		return new Filters(filters);
	}
}
