package io.github.willypol78.criteria.to.specifications;

import io.github.willypol78.criteria.operands.relational.ContainsStrictOperand;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public final class ContainsStrictSpecification<T> implements Specification<T> {
	private final transient ContainsStrictOperand operand;

	public ContainsStrictSpecification(final ContainsStrictOperand operand) {this.operand = operand;}

	@Override
	public Predicate toPredicate(@NonNull final Root<T> root, @Nullable final CriteriaQuery<?> query, @NonNull final CriteriaBuilder criteriaBuilder) {
		return criteriaBuilder.like(root.get(operand.field()), "%" + operand.value() + "%");
	}
}
