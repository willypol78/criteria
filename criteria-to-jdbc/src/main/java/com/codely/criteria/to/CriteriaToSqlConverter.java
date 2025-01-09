package com.codely.criteria.to;


import com.codely.criteria.Criteria;
import com.codely.criteria.CriteriaToFilterOperandConverter;
import com.codely.criteria.errors.InvalidFilter;
import com.codely.criteria.operands.FilterOperand;
import com.codely.criteria.operands.logical.AndFilterOperand;
import com.codely.criteria.operands.logical.LogicalFilterOperand;
import com.codely.criteria.operands.logical.NotFilterOperand;
import com.codely.criteria.operands.logical.OrFilterOperand;
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
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CriteriaToSqlConverter {

	private final SQLTransformer sqlTransformer;
	private       int            next = 0;

	public CriteriaToSqlConverter(final SQLTransformer sqlTransformer) {
		this.sqlTransformer = sqlTransformer;
	}

	public SqlAndParams convert(String tableName, Criteria criteria) throws InvalidFilter {
		FilterOperand filterOperand = new CriteriaToFilterOperandConverter().convert(criteria);

		String       select = "SELECT * FROM " + tableName + " WHERE 1=1 AND ";
		SqlAndParams where  = operandToSql(filterOperand);
		String       order  = "";
		if (!criteria.orderType().isNone()) {
			order = " ORDER BY " + criteria.orderBy().value() + " " + criteria.orderType().value();
		}
		String size = sqlTransformer.limitAndOffset(criteria.pageNumber(), criteria.pageSize());

		return new SqlAndParams(select + where.sql() + order + size, where.params());
	}

	private SqlAndParams operandToSql(final FilterOperand operand) {
		return switch (operand) {
			case AndFilterOperand(var o) -> getSqlAndParams(o, " AND ");
			case OrFilterOperand(var o) -> getSqlAndParams(o, " OR ");
			case NotFilterOperand o -> {
				SqlAndParams op = convertQuery(o.operands().getFirst());
				yield new SqlAndParams(" NOT (" + op.sql() + ")", op.params());
			}
			default -> convertQuery(operand);
		};
	}

	private SqlAndParams getSqlAndParams(final List<FilterOperand> filterOperands, final String logicalOperator) {
		SqlAndParams result = convertQuery(filterOperands.getFirst());
		for (int i = 1; i < filterOperands.size(); i++) {
			SqlAndParams nextSqlParam   = convertQuery(filterOperands.get(i));
			boolean      hasParenthesis = filterOperands.get(i) instanceof LogicalFilterOperand;
			result = new SqlAndParams(
					result.sql() + logicalOperator + open(hasParenthesis) + nextSqlParam.sql() + close(hasParenthesis),
					Stream.concat(result.params().entrySet().stream(),
					              nextSqlParam.params().entrySet().stream())
							.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
			);
		}
		return result;
	}

	private String open(boolean parenthesis) {
		return parenthesis ? "(" : "";
	}

	private String close(boolean parenthesis) {
		return parenthesis ? ")" : "";
	}

	private SqlAndParams convertQuery(final FilterOperand operand) {
		return switch (operand) {
			case ContainsOperand(String field, Serializable value) -> {
				String fieldParam = ":" + field + "_" + next++;
				yield new SqlAndParams(field + " LIKE *" + fieldParam + "*", Map.of(fieldParam, value));
			}
			case ContainsStrictOperand(String field, Serializable value) -> {
				String fieldParam = ":" + field + "_" + next++;
				yield new SqlAndParams("UPPER(" + field + ") LIKE  *" + fieldParam + "*", Map.of(fieldParam, value.toString().toUpperCase()));
			}
			case EqualsOperand(String field, Serializable value) -> {
				String fieldParam = ":" + field + "_" + next++;
				yield new SqlAndParams("UPPER(" + field + ") = " + fieldParam, Map.of(fieldParam, value.toString().toUpperCase()));
			}
			case EqualsStrictOperand(String field, Serializable value) -> {
				String fieldParam = ":" + field + "_" + next++;
				yield new SqlAndParams(field + " = " + fieldParam, Map.of(fieldParam, value));
			}
			case GreaterThanEqualToOperand(String field, Serializable value) -> {
				String fieldParam = ":" + field + "_" + next++;
				yield new SqlAndParams(field + " >= " + fieldParam, Map.of(fieldParam, value));
			}
			case GreaterThanOperand(String field, Serializable value) -> {
				String fieldParam = ":" + field + "_" + next++;
				yield new SqlAndParams(field + " > " + fieldParam, Map.of(fieldParam, value));
			}
			case LowerThanEqualOperand(String field, Serializable value) -> {
				String fieldParam = ":" + field + "_" + next++;
				yield new SqlAndParams(field + " <= " + fieldParam, Map.of(fieldParam, value));
			}
			case LowerThanOperand(String field, Serializable value) -> {
				String fieldParam = ":" + field + "_" + next++;
				yield new SqlAndParams(field + " < " + fieldParam, Map.of(fieldParam, value));
			}
			case NotEqualsOperand(String field, Serializable value) -> {
				String fieldParam = ":" + field + "_" + next++;
				yield new SqlAndParams(field + " != " + fieldParam, Map.of(fieldParam, value));
			}
			case NotEqualsStrictOperand(String field, Serializable value) -> {
				String fieldParam = ":" + field + "_" + next++;
				yield new SqlAndParams(field + " != " + fieldParam, Map.of(fieldParam, value));
			}
			case RegExpOperand(String field, Serializable value) -> {
				String fieldParam = ":" + field + "_" + next++;
				yield new SqlAndParams(field + " ~ " + fieldParam, Map.of(fieldParam, value));
			}
			default -> operandToSql(operand);
		};
	}
}
