package io.github.willypol78.criteria.filters;

/**
 * Composite filter that represents the logical conjunction (AND) of multiple filters.
 * <p>
 * This class can be used to combine several {@link Filter} instances
 * and apply all filtering criteria together.
 * </p>
 *
 * @author Guillermo Mir
 * @since 1.0.0
 */
public final class AndFilter implements Filter {
    /**
     * Creates an instance of {@code AndFilter}.
     */
    public AndFilter() {
        // Default constructor
    }
}
