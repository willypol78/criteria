package io.github.willypol78.criteria.to.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

/**
 * A specification that always evaluates to true, effectively acting as a no-op.
 * This can be useful when you need a placeholder specification that does not filter results.
 *
 * @param <T> the type of the entity
 */
@SuppressWarnings("all")
public final class VoidSpecification<T> implements Specification<T> {

	/**
	 * Constructs a new {@code VoidSpecification}.
	 */
	public VoidSpecification() {
		// Default constructor
	}

	@Override
	public Predicate toPredicate(final Root<T> root, final @NonNull CriteriaQuery<?> query, final CriteriaBuilder criteriaBuilder) {
		return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
	}
}
