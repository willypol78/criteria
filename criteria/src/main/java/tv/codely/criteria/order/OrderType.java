package tv.codely.criteria.order;

public enum OrderType {
	ASC("asc"),
	DESC("desc"),
	NONE("none");
	private final String type;

	OrderType(String type) {
		this.type = type;
	}

	public String value() {
		return type;
	}

	public boolean isNone() {
		return this == NONE;
	}
}
