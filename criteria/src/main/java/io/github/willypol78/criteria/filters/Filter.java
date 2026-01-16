package io.github.willypol78.criteria.filters;

public sealed interface Filter permits AndFilter, CloseParenthesisFilter, ComparatorFilter, NotFilter, OpenParenthesisFilter, OrFilter {
	static Filter and() {
		return new AndFilter();
	}

	static Filter or() {
		return new OrFilter();
	}

	static Filter not() {
		return new NotFilter();
	}

	static Filter openParenthesis() {
		return new OpenParenthesisFilter();
	}

	static Filter closeParenthesis() {
		return new CloseParenthesisFilter();
	}
}
