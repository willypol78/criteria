package com.codely.criteria.to.specifications;

import com.codely.criteria.operands.relational.EqualsStrictOperand;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import java.util.Objects;
import java.util.UUID;

public final class EqualsStrictSpecification<T> implements Specification<T> {
	private final transient EqualsStrictOperand operand;

	public EqualsStrictSpecification(final EqualsStrictOperand operand) {this.operand = operand;}

	@Override
	public Predicate toPredicate(@NonNull final Root<T> root, @Nullable final CriteriaQuery<?> query, @NonNull final CriteriaBuilder criteriaBuilder) {
		if (Objects.requireNonNull(operand.value()) instanceof UUID uuid) {
			return criteriaBuilder.equal(root.get(operand.field()).as(UUID.class), uuid);
		}
		return criteriaBuilder.equal(root.get(operand.field()), operand.value());
	}
}
