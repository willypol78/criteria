package dev.willypol.order;

/**
 * Represents an order by clause in a query.
 * <p>
 * This class is used to specify the ordering of results based on a given field.
 * </p>
 *
 * @param value the field by which to order the results
 * @author Guillermo Mir
 * @since 1.0.0
 */
public record OrderBy(String value) { }
