package dev.willypol.filters;

/**
 * Composite filter that represents an open parenthesis in a filter expression.
 * <p>
 * This class can be used to indicate the start of a grouping of filters
 * in a complex filtering expression.
 * </p>
 *
 * @author Guillermo Mir
 * @since 1.0.0
 */
public final class OpenParenthesisFilter implements Filter {
    /**
     * Creates an instance of {@code OpenParenthesisFilter}.
     */
    public OpenParenthesisFilter() {
        // Default constructor
    }
}
