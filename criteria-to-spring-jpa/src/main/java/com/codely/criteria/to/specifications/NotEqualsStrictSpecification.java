package com.codely.criteria.to.specifications;

import com.codely.criteria.operands.relational.NotEqualsStrictOperand;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import java.util.UUID;

public final class NotEqualsStrictSpecification<T> implements Specification<T> {
	private final transient NotEqualsStrictOperand operand;

	public NotEqualsStrictSpecification(final NotEqualsStrictOperand operand) {this.operand = operand;}

	@Override
	public Predicate toPredicate(final Root<T> root, final @NonNull CriteriaQuery<?> query, final CriteriaBuilder criteriaBuilder) {
		return switch (operand.value()) {
			case UUID uuid -> criteriaBuilder.notEqual(root.get(operand.field()).as(UUID.class), uuid);
			default -> criteriaBuilder.notEqual(root.get(operand.field()), operand.value());
		};
	}
}
