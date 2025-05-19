package dev.willypol.to.specifications;

import dev.willypol.operands.relational.NotEqualsStrictOperand;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import java.util.Objects;
import java.util.UUID;

public final class NotEqualsStrictSpecification<T> implements Specification<T> {
	private final transient NotEqualsStrictOperand operand;

	public NotEqualsStrictSpecification(final NotEqualsStrictOperand operand) {this.operand = operand;}

	@Override
	public Predicate toPredicate(@NonNull final Root<T> root, @Nullable final CriteriaQuery<?> query, @NonNull final CriteriaBuilder criteriaBuilder) {
		if (Objects.requireNonNull(operand.value()) instanceof UUID uuid) {
			return criteriaBuilder.notEqual(root.get(operand.field()).as(UUID.class), uuid);
		}
		return criteriaBuilder.notEqual(root.get(operand.field()), operand.value());
	}
}
