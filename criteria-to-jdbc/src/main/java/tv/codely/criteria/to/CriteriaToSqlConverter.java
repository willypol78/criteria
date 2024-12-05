package tv.codely.criteria.to;


import tv.codely.criteria.Criteria;
import tv.codely.criteria.CriteriaToFilterOperandConverter;
import tv.codely.criteria.errors.InvalidFilter;
import tv.codely.criteria.operands.FilterOperand;
import tv.codely.criteria.operands.logical.AndFilterOperand;
import tv.codely.criteria.operands.logical.LogicalFilterOperand;
import tv.codely.criteria.operands.logical.NotFilterOperand;
import tv.codely.criteria.operands.logical.OrFilterOperand;
import tv.codely.criteria.operands.relational.ContainsOperand;
import tv.codely.criteria.operands.relational.ContainsStrictOperand;
import tv.codely.criteria.operands.relational.EqualsOperand;
import tv.codely.criteria.operands.relational.EqualsStrictOperand;
import tv.codely.criteria.operands.relational.GreaterThanEqualToOperand;
import tv.codely.criteria.operands.relational.GreaterThanOperand;
import tv.codely.criteria.operands.relational.LowerThanEqualOperand;
import tv.codely.criteria.operands.relational.LowerThanOperand;
import tv.codely.criteria.operands.relational.NotEqualsOperand;
import tv.codely.criteria.operands.relational.NotEqualsStrictOperand;
import tv.codely.criteria.operands.relational.RegExpOperand;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CriteriaToSqlConverter {

	private int next = 0;

	public SqlAndParams convert(String tableName, Criteria criteria) throws InvalidFilter {
		FilterOperand filterOperand = new CriteriaToFilterOperandConverter().convert(criteria);

		String       select = "SELECT * FROM " + tableName + " WHERE 1=1 AND ";
		SqlAndParams where  = operandToSql(filterOperand);
		String       order  = "";
		String       size   = "";
		if (!criteria.orderType().isNone()) {
			order = " ORDER BY " + criteria.orderBy().value() + " " + criteria.orderType().value();
		}
		if (criteria.pageSize() > 0) {
			size = " LIMIT " + criteria.pageSize();
			if (criteria.pageNumber() > 0) {
				size += " OFFSET " + criteria.pageNumber() * criteria.pageSize();
			}
		}

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
