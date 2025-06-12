package dev.willypol.filters;

import java.util.Optional;

/**
 * Represents a primitive filter with a field, operator, and optional value.
 * <p>
 * This record is used to encapsulate the basic components of a filter
 * that can be used in a filtering expression.
 * </p>
 *
 * @param field    The field to filter on, optional.
 * @param operator The operator to apply (e.g., "=", "&gt;", "&lt;", etc.).
 * @param value    The value to compare against, optional.
 * @author Guillermo Mir
 * @since 1.0.0
 */
public record FilterPrimitives(Optional<String> field, String operator, Optional<String> value) {}
