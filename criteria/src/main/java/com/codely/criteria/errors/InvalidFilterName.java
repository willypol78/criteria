package com.codely.criteria.errors;

public final class InvalidFilterName extends InvalidFilter {
	public InvalidFilterName() {
		super("The field name to filter is invalid");
	}
}
