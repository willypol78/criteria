package dev.willypol.filters;

/**
 * Composite filter that represents the logical conjunction (OR) of multiple filters.
 * <p>
 * This class can be used to combine several {@link Filter} instances
 * and apply all filtering criteria together.
 * </p>
 *
 * @author Guillermo Mir
 * @since 1.0.0
 */
public final class OrFilter implements Filter {
    /**
     * Creates an instance of {@code OrFiler}.
     */
    public OrFilter() {
        // Default constructor
    }
}
