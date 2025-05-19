package dev.willypol.to;

public final class PostgreSQLTransformer implements SQLTransformer {
	@Override
	public String limitAndOffset(final Integer pageNumber, final Integer pageSize) {
		if (pageSize > 0) {
			String size = " LIMIT " + pageSize;
			if (pageNumber > 0) {
				size += " OFFSET " + pageNumber * pageSize;
			}
			return size;
		} else {
			return "";
		}
	}
}
