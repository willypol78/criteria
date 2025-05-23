package dev.willypol;

import dev.willypol.errors.InvalidCriteria;
import dev.willypol.errors.InvalidFilter;
import dev.willypol.filters.Filter;
import dev.willypol.filters.FilterPrimitives;
import dev.willypol.filters.Filters;
import dev.willypol.order.Order;
import dev.willypol.order.OrderBy;
import dev.willypol.order.OrderType;
import java.util.List;
import java.util.Objects;


public final class Criteria {
	private final Filters filters;
	private final Order   order;
	private final Integer pageSize;
	private final Integer pageNumber;


	public Criteria(final Filters filters, final Order order, final Integer pageSize, final Integer pageNumber) throws InvalidCriteria {
		checkIsValidCriteria(pageSize, pageNumber);
		this.filters = filters;
		this.order = order;
		this.pageSize = pageSize;
		this.pageNumber = pageNumber;
	}

	public static Criteria fromPrimitives(
			final List<FilterPrimitives> filters,
			final String orderBy,
			final String orderType,
			final Integer pageSize,
			final Integer pageNumber)
			throws InvalidCriteria {

		try {
			return new Criteria(Filters.fromPrimitives(filters), Order.fromPrimitives(orderBy, orderType), pageSize, pageNumber);
		} catch (InvalidFilter e) {
			throw new InvalidCriteria(e.getMessage());
		}
	}

	public List<Filter> filters() {
		return filters.filters();
	}

	public int filterSize() {
		return filters.size();
	}

	public OrderBy orderBy() {
		return order.orderBy();
	}

	public OrderType orderType() {
		return order.orderType();
	}

	public Integer pageSize() {
		return pageSize;
	}

	public Integer pageNumber() {
		return Objects.nonNull(pageNumber) ? pageNumber : 0;
	}

	private void checkIsValidCriteria(final Integer pageSize, final Integer pageNumber) throws InvalidCriteria {
		if (Objects.nonNull(pageNumber) && Objects.isNull(pageSize)) {
			throw new InvalidCriteria("Page number is set but page size is not");
		}
	}
}
