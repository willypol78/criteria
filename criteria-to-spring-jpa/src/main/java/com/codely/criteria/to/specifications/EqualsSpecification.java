package com.codely.criteria.to.specifications;

import com.codely.criteria.operands.relational.EqualsOperand;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import java.util.UUID;

public final class EqualsSpecification<T> implements Specification<T> {
	private final transient EqualsOperand operand;

	public EqualsSpecification(final EqualsOperand operand) {this.operand = operand;}

	@Override
	public Predicate toPredicate(final Root<T> root, final @NonNull CriteriaQuery<?> query, final CriteriaBuilder criteriaBuilder) {
		return switch (operand.value()) {
			case UUID uuid -> criteriaBuilder.equal(root.get(operand.field()).as(UUID.class), uuid);
			case String s -> criteriaBuilder.equal(criteriaBuilder.upper(root.get(operand.field())), s.toUpperCase());
			default -> criteriaBuilder.equal(root.get(operand.field()), operand.value());
		};
	}
}
