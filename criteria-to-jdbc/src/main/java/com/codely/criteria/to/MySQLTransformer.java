package com.codely.criteria.to;

public final class MySQLTransformer implements SQLTransformer {
	@Override
	public String limitAndOffset(final Integer pageNumber, final Integer pageSize) {
		if (pageSize > 0) {
			String size = " LIMIT " + pageSize;
			if (pageNumber > 0) {
				size += ", " + pageNumber * pageSize;
			}
			return size;
		} else {
			return "";
		}
	}
}
