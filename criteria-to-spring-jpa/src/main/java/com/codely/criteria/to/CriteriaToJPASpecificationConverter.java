package com.codely.criteria.to;


import com.codely.criteria.Criteria;
import com.codely.criteria.CriteriaToFilterOperandConverter;
import com.codely.criteria.errors.InvalidFilter;
import com.codely.criteria.operands.FilterOperand;
import com.codely.criteria.operands.logical.AndFilterOperand;
import com.codely.criteria.operands.logical.NotFilterOperand;
import com.codely.criteria.operands.logical.OrFilterOperand;
import com.codely.criteria.operands.logical.VoidFilterOperand;
import com.codely.criteria.operands.relational.ContainsOperand;
import com.codely.criteria.operands.relational.ContainsStrictOperand;
import com.codely.criteria.operands.relational.EqualsOperand;
import com.codely.criteria.operands.relational.EqualsStrictOperand;
import com.codely.criteria.operands.relational.GreaterThanEqualToOperand;
import com.codely.criteria.operands.relational.GreaterThanOperand;
import com.codely.criteria.operands.relational.LowerThanEqualOperand;
import com.codely.criteria.operands.relational.LowerThanOperand;
import com.codely.criteria.operands.relational.NotEqualsOperand;
import com.codely.criteria.operands.relational.NotEqualsStrictOperand;
import com.codely.criteria.operands.relational.RegExpOperand;
import com.codely.criteria.order.OrderType;
import com.codely.criteria.to.specifications.ContainsSpecification;
import com.codely.criteria.to.specifications.ContainsStrictSpecification;
import com.codely.criteria.to.specifications.EqualsSpecification;
import com.codely.criteria.to.specifications.EqualsStrictSpecification;
import com.codely.criteria.to.specifications.GreaterThanEqualSpecification;
import com.codely.criteria.to.specifications.GreaterThanSpecification;
import com.codely.criteria.to.specifications.LowerThanEqualSpecification;
import com.codely.criteria.to.specifications.LowerThanSpecification;
import com.codely.criteria.to.specifications.NotEqualsSpecification;
import com.codely.criteria.to.specifications.NotEqualsStrictSpecification;
import com.codely.criteria.to.specifications.RegExpSpecification;
import com.codely.criteria.to.specifications.VoidSpecification;
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
