package tv.codely.criteria;

import tv.codely.criteria.errors.InvalidCriteria;
import tv.codely.criteria.filters.Filters;
import tv.codely.criteria.order.Order;

public final class CriteriaMother {

	private Filters filters;
	private Order   order;
	private Integer pageSize;
	private Integer pageNumber;

	private CriteriaMother() {
		this.filters = Filters.empty();
		this.order = Order.none();
		this.pageSize = null;
		this.pageNumber = null;
	}

	public static CriteriaMother create() {
		return new CriteriaMother();
	}

	public CriteriaMother withFilters(Filters filters) {
		this.filters = filters;
		return this;
	}

	public CriteriaMother withOrder(Order order) {
		this.order = order;
		return this;
	}

	public CriteriaMother withPageSize(Integer pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	public CriteriaMother withPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
		return this;
	}

	public Criteria build() throws InvalidCriteria {
		return new Criteria(filters, order, pageSize, pageNumber);
	}
}
