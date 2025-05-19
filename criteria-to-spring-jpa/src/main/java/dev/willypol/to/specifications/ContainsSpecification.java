package dev.willypol.to.specifications;


import dev.willypol.operands.relational.ContainsOperand;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import java.util.Objects;

public final class ContainsSpecification<T> implements Specification<T> {
	private final transient ContainsOperand operand;

	public ContainsSpecification(final ContainsOperand operand) {this.operand = operand;}

	@Override
	public Predicate toPredicate(@NonNull final Root<T> root, final @Nullable CriteriaQuery<?> query, @NonNull final CriteriaBuilder criteriaBuilder) {
		if (Objects.requireNonNull(operand.value()) instanceof String s) {
			return criteriaBuilder.like(criteriaBuilder.upper(root.get(operand.field())), "%" + s.toUpperCase() + "%");
		}
		return criteriaBuilder.like(root.get(operand.field()), "%" + operand.value() + "%");
	}
}
