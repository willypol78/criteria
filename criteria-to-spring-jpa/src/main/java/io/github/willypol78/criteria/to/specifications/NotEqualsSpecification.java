package io.github.willypol78.criteria.to.specifications;

import io.github.willypol78.criteria.operands.relational.NotEqualsOperand;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import java.util.UUID;

public final class NotEqualsSpecification<T> implements Specification<T> {
	private final transient NotEqualsOperand operand;

	public NotEqualsSpecification(final NotEqualsOperand operand) {this.operand = operand;}

	@Override
	public Predicate toPredicate(@NonNull final Root<T> root, final @Nullable CriteriaQuery<?> query, @NonNull final CriteriaBuilder criteriaBuilder) {
		return switch (operand.value()) {
			case UUID uuid -> criteriaBuilder.notEqual(root.get(operand.field()).as(UUID.class), uuid);
			case String s -> criteriaBuilder.notEqual(criteriaBuilder.upper(root.get(operand.field())), s.toUpperCase());
			default -> criteriaBuilder.notEqual(root.get(operand.field()), operand.value());
		};
	}
}
