package dev.willypol.to.specifications;

import dev.willypol.operands.relational.LowerThanOperand;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public final class LowerThanSpecification<T> implements Specification<T> {
	private final transient LowerThanOperand operand;

	public LowerThanSpecification(final LowerThanOperand operand) {this.operand = operand;}

	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public Predicate toPredicate(@NonNull final Root<T> root, @Nullable final CriteriaQuery<?> query, @NonNull final CriteriaBuilder criteriaBuilder) {
		return criteriaBuilder.lessThan(root.get(operand.field()), (Comparable) operand.value());
	}
}
