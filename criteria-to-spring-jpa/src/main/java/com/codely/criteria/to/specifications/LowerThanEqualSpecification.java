package com.codely.criteria.to.specifications;

import com.codely.criteria.operands.relational.LowerThanEqualOperand;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public final class LowerThanEqualSpecification<T> implements Specification<T> {
	private final transient LowerThanEqualOperand operand;

	public LowerThanEqualSpecification(final LowerThanEqualOperand operand) {this.operand = operand;}

	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public Predicate toPredicate(@NonNull final Root<T> root, @Nullable final CriteriaQuery<?> query, @NonNull final CriteriaBuilder criteriaBuilder) {
		return criteriaBuilder.lessThanOrEqualTo(root.get(operand.field()), (Comparable) operand.value());
	}
}
