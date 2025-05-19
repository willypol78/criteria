package dev.willypol.order;


import java.util.Objects;

public record Order(OrderBy orderBy, OrderType orderType) {

	public static Order fromPrimitives(String orderBy, String orderType) throws IllegalArgumentException {
		OrderBy   order = Objects.nonNull(orderBy) ? new OrderBy(orderBy) : new OrderBy("");
		OrderType type  = Objects.nonNull(orderType) ? OrderType.valueOf(orderType) : OrderType.NONE;
		return new Order(order, type);
	}

	public static Order none() {
		return new Order(new OrderBy(""), OrderType.NONE);
	}

	public static Order desc(String orderBy) {
		return new Order(new OrderBy(orderBy), OrderType.DESC);
	}

	public static Order asc(String orderBy) {
		return new Order(new OrderBy(orderBy), OrderType.ASC);
	}

	public boolean hasOrder() {
		return !orderType.isNone();
	}

	public String serialize() {
		return String.format("%s.%s", orderBy.value(), orderType.value());
	}
}
