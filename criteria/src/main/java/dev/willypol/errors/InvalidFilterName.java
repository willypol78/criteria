package dev.willypol.errors;

public final class InvalidFilterName extends InvalidFilter {
	public InvalidFilterName() {
		super("The field name to filter is invalid");
	}
}
