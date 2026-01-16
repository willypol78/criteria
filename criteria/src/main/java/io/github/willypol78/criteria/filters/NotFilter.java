package io.github.willypol78.criteria.filters;

/**
 * Composite filter that represents the logical negation (NOT) of multiple filters.
 * <p>
 * This class can be used to combine several {@link Filter} instances
 * and apply all filtering criteria together.
 * </p>
 *
 * @author Guillermo Mir
 * @since 1.0.0
 */
public final class NotFilter implements Filter {
    /**
     * Creates an instance of {@code NotFilter}.
     */
    public NotFilter() {
        // Default constructor
    }
}
