package dev.willypol.to;


import dev.willypol.Criteria;
import dev.willypol.CriteriaToFilterOperandConverter;
import dev.willypol.errors.InvalidFilter;
import dev.willypol.operands.FilterOperand;
import dev.willypol.operands.logical.AndFilterOperand;
import dev.willypol.operands.logical.NotFilterOperand;
import dev.willypol.operands.logical.OrFilterOperand;
import dev.willypol.operands.logical.VoidFilterOperand;
import dev.willypol.operands.relational.ContainsOperand;
import dev.willypol.operands.relational.ContainsStrictOperand;
import dev.willypol.operands.relational.EqualsOperand;
import dev.willypol.operands.relational.EqualsStrictOperand;
import dev.willypol.operands.relational.GreaterThanEqualToOperand;
import dev.willypol.operands.relational.GreaterThanOperand;
import dev.willypol.operands.relational.LowerThanEqualOperand;
import dev.willypol.operands.relational.LowerThanOperand;
import dev.willypol.operands.relational.NotEqualsOperand;
import dev.willypol.operands.relational.NotEqualsStrictOperand;
import dev.willypol.operands.relational.RegExpOperand;
import dev.willypol.order.OrderType;
import dev.willypol.to.specifications.ContainsSpecification;
import dev.willypol.to.specifications.ContainsStrictSpecification;
import dev.willypol.to.specifications.EqualsSpecification;
import dev.willypol.to.specifications.EqualsStrictSpecification;
import dev.willypol.to.specifications.GreaterThanEqualSpecification;
import dev.willypol.to.specifications.GreaterThanSpecification;
import dev.willypol.to.specifications.LowerThanEqualSpecification;
import dev.willypol.to.specifications.LowerThanSpecification;
import dev.willypol.to.specifications.NotEqualsSpecification;
import dev.willypol.to.specifications.NotEqualsStrictSpecification;
import dev.willypol.to.specifications.RegExpSpecification;
import dev.willypol.to.specifications.VoidSpecification;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import java.util.Objects;

/**
 * Esta es una clase final que convierte los criterios de filtro en especificaciones JPA.
 * Esta clase no puede ser instanciada y solo proporciona métodos estáticos.
 */
public final class CriteriaToJPASpecificationConverter {
	private static final Integer DEFAULT_PAGE_SIZE = 20;

	private CriteriaToJPASpecificationConverter() {}

	public static <T> Specification<T> toSpecification(Criteria criteria) throws InvalidFilter {
		try {
			FilterOperand result = new CriteriaToFilterOperandConverter().convert(criteria);
			return convertSpec(result);
		} catch (Exception e) {
			throw new InvalidFilter("The created filter criteria is not valid");
		}
	}


	public static Pageable toPageable(Criteria criteria) {
		Sort sort = Sort.unsorted();
		if (Objects.nonNull(criteria.orderBy()) && Strings.isNotBlank(criteria.orderBy().value())) {
			String         field     = criteria.orderBy().value();
			Sort.Direction direction = criteria.orderType() == OrderType.ASC ? Sort.Direction.ASC : Sort.Direction.DESC;
			sort = Sort.by(direction, field);
		}
		return PageRequest.of(criteria.pageNumber(), Objects.nonNull(criteria.pageSize()) ? criteria.pageSize() : DEFAULT_PAGE_SIZE, sort);
	}


	@SuppressWarnings("unchecked")
	private static <T> Specification<T> convertSpec(FilterOperand operand) {
		return switch (operand) {
			case VoidFilterOperand s -> new VoidSpecification<>();
			case AndFilterOperand s ->
					(Specification<T>) Specification.allOf(operand.operands().stream().map(CriteriaToJPASpecificationConverter::convert).toList());
			case OrFilterOperand s ->
					(Specification<T>) Specification.anyOf(operand.operands().stream().map(CriteriaToJPASpecificationConverter::convert).toList());
			case NotFilterOperand s -> Specification.not(convert(operand.operands().getFirst()));
			default -> convert(operand);
		};
	}

	private static <T> Specification<T> convert(FilterOperand operand) {
		return switch (operand) {
			case ContainsStrictOperand s -> new ContainsStrictSpecification<>(s);
			case ContainsOperand s -> new ContainsSpecification<>(s);
			case EqualsStrictOperand s -> new EqualsStrictSpecification<>(s);
			case EqualsOperand s -> new EqualsSpecification<>(s);
			case GreaterThanEqualToOperand s -> new GreaterThanEqualSpecification<>(s);
			case GreaterThanOperand s -> new GreaterThanSpecification<>(s);
			case LowerThanEqualOperand s -> new LowerThanEqualSpecification<>(s);
			case LowerThanOperand s -> new LowerThanSpecification<>(s);
			case NotEqualsOperand s -> new NotEqualsSpecification<>(s);
			case NotEqualsStrictOperand s -> new NotEqualsStrictSpecification<>(s);
			case RegExpOperand s -> new RegExpSpecification<>(s);
			default -> convertSpec(operand);
		};
	}
}
