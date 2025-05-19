package dev.willypol.to.specifications;

import dev.willypol.operands.relational.RegExpOperand;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public final class RegExpSpecification<T> implements Specification<T> {
	private final transient RegExpOperand operand;

	public RegExpSpecification(final RegExpOperand operand) {
		this.operand = operand;
	}

	@Override
	public Predicate toPredicate(@NonNull final Root<T> root, @Nullable final CriteriaQuery<?> query, @NonNull final CriteriaBuilder criteriaBuilder) {
		Expression<Boolean> exp = criteriaBuilder.function("textregexeq", Boolean.class, root.get(operand.field()), criteriaBuilder.literal(operand.value()));
		return criteriaBuilder.isTrue(exp);
	}
}
