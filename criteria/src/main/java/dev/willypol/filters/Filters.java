package dev.willypol.filters;

import dev.willypol.errors.InvalidFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a collection of filters that can be used to build complex filtering expressions.
 * <p>
 * This class provides methods to create filters from primitive filter representations,
 * check if the collection is empty, and retrieve the list of filters.
 * </p>
 *
 * @author Guillermo Mir
 * @since 1.0.0
 */
public final class Filters {

	private final List<Filter> filterList;

	public Filters(final List<Filter> filterList) {
		this.filterList = filterList;
	}

	public static Filters fromPrimitives(final List<FilterPrimitives> filterPrimitives) throws InvalidFilter {
		List<Filter> filterList = new ArrayList<>();
		for (FilterPrimitives f : filterPrimitives) {
			switch (f.operator().toUpperCase()) {
				case "(":
					filterList.add(new OpenParenthesisFilter());
					break;
				case ")":
					filterList.add(new CloseParenthesisFilter());
					break;
				case "AND":
					filterList.add(new AndFilter());
					break;
				case "OR":
					filterList.add(new OrFilter());
					break;
				case "NOT":
					filterList.add(new NotFilter());
					break;
				default:
					filterList.add(ComparatorFilter.fromPrimitives(f));
			}
		}
		return new Filters(filterList);
	}

	public static Filters empty() {
		return new Filters(new ArrayList<>());
	}

	public List<Filter> filters() {
		return filterList;
	}

	public boolean isEmpty() {
		return filterList.isEmpty();
	}

	public int size() {
		return filterList.size();
	}


}
