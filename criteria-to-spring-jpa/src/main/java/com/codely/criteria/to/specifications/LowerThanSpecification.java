package com.codely.criteria.to.specifications;

import com.codely.criteria.operands.relational.LowerThanOperand;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

public final class LowerThanSpecification<T> implements Specification<T> {
	private final transient LowerThanOperand operand;

	public LowerThanSpecification(final LowerThanOperand operand) {this.operand = operand;}

	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public Predicate toPredicate(final Root<T> root, final @NonNull CriteriaQuery<?> query, final CriteriaBuilder criteriaBuilder) {
		return criteriaBuilder.lessThan(root.get(operand.field()), (Comparable) operand.value());
	}
}
