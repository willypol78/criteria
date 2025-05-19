package dev.willypol.from;

import dev.willypol.Criteria;
import dev.willypol.errors.InvalidCriteria;
import dev.willypol.filters.FilterPrimitives;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;


public final class CriteriaFromHttpServletRequestUriConverter {

	private static final String FILTERS  = "f";
	private static final String FIELD    = "f";
	private static final String OPERATOR = "o";
	private static final String VALUE    = "v";

	public Criteria toCriteria(HttpServletRequest getRequest) throws InvalidCriteria {

		List<FilterPrimitives> filters = toFilterPrimitivesListFromUriParams(getRequest);

		String  orderBy    = getRequest.getParameter("orderBy");
		String  orderType  = getRequest.getParameter("orderType");
		Integer pageSize   = Optional.ofNullable(getRequest.getParameter("size")).map(Integer::parseInt).orElse(null);
		Integer pageNumber = Optional.ofNullable(getRequest.getParameter("page")).map(Integer::parseInt).orElse(null);

		return Criteria.fromPrimitives(filters, orderBy, orderType, pageSize, pageNumber);
	}

	private List<FilterPrimitives> toFilterPrimitivesListFromUriParams(HttpServletRequest request) {

		List<FilterPrimitives> filters = new ArrayList<>();

		IntStream.iterate(0, i -> i + 1)
				.mapToObj(i -> new FilterPrimitives(
						Optional.ofNullable(request.getParameter(FILTERS + "[" + i + "][" + FIELD + "]")),
						request.getParameter(FILTERS + "[" + i + "][" + OPERATOR + "]"),
						Optional.ofNullable(request.getParameter(FILTERS + "[" + i + "][" + VALUE + "]"))
				))
				.takeWhile(fp -> fp.operator() != null)
				.forEach(filters::add);

		return filters;
	}
}
