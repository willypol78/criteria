package com.codely.criteria.to.specifications;

import com.codely.criteria.operands.relational.ContainsStrictOperand;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

public final class ContainsStrictSpecification<T> implements Specification<T> {
	private final transient ContainsStrictOperand operand;

	public ContainsStrictSpecification(final ContainsStrictOperand operand) {this.operand = operand;}

	@Override
	public Predicate toPredicate(final Root<T> root, final @NonNull CriteriaQuery<?> query, final CriteriaBuilder criteriaBuilder) {
		return criteriaBuilder.like(root.get(operand.field()), "%" + operand.value() + "%");
	}
}
