package com.codely.criteria.to.specifications;


import com.codely.criteria.operands.relational.ContainsOperand;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

public final class ContainsSpecification<T> implements Specification<T> {
	private final transient ContainsOperand operand;

	public ContainsSpecification(final ContainsOperand operand) {this.operand = operand;}

	@Override
	public Predicate toPredicate(final Root<T> root, final @NonNull CriteriaQuery<?> query, final CriteriaBuilder criteriaBuilder) {
		return switch (operand.value()) {
			case String s -> criteriaBuilder.like(criteriaBuilder.upper(root.get(operand.field())), "%" + s.toUpperCase() + "%");
			default -> criteriaBuilder.like(root.get(operand.field()), "%" + operand.value() + "%");
		};
	}
}
