package tv.codely.criteria.to;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.RegexpQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.WildcardQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import jakarta.json.stream.JsonGenerator;
import tv.codely.criteria.Criteria;
import tv.codely.criteria.CriteriaToFilterOperandConverter;
import tv.codely.criteria.errors.InvalidFilter;
import tv.codely.criteria.operands.FilterOperand;
import tv.codely.criteria.operands.logical.AndFilterOperand;
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
import java.io.StringWriter;
import java.util.List;
import java.util.Objects;


public final class CriteriaToElasticsearchConverter {

	private CriteriaToElasticsearchConverter() {}

	public static Query toQuery(final Criteria criteria) throws InvalidFilter {
		FilterOperand filterOperand = new CriteriaToFilterOperandConverter().convert(criteria);
		return filterOperandToQuery(filterOperand);
	}

	private static List<SortOptions> toSortOptions(final Criteria criteria) {
		if (criteria.orderType().isNone()) {
			return List.of();
		}
		String      fieldToSort = criteria.orderBy().value();
		SortOrder   sortOrder   = SortOrder.valueOf(capitalizeFirstLetter(criteria.orderType().value()));
		SortOptions sortOptions = SortOptions.of(s -> s.field(FieldSort.of(f -> f.field(fieldToSort).order(sortOrder))));
		return List.of(sortOptions);
	}

	public static String toQueryString(final Criteria criteria) throws InvalidFilter {
		Query             query       = CriteriaToElasticsearchConverter.toQuery(criteria);
		List<SortOptions> sortOptions = CriteriaToElasticsearchConverter.toSortOptions(criteria);

		SearchRequest.Builder searchRequestBuilder = new SearchRequest.Builder();
		searchRequestBuilder.query(query);
		if (!sortOptions.isEmpty()) {
			searchRequestBuilder.sort(sortOptions);
		}
		if (Objects.nonNull(criteria.pageSize())) {
			searchRequestBuilder.from(criteria.pageNumber() * criteria.pageSize());
			searchRequestBuilder.size(criteria.pageSize());
		}

		return searchRequestToElasticsearchQueryJsonDsl(searchRequestBuilder.build());
	}

	private static Query filterOperandToQuery(FilterOperand operand) {
		return switch (operand) {
			case AndFilterOperand(var o) -> {
				List<Query> queryList = o.stream().map(CriteriaToElasticsearchConverter::convertQuery).toList();
				yield BoolQuery.of(b -> b.must(queryList))._toQuery();
			}
			case OrFilterOperand(var o) -> {
				List<Query> queryList = o.stream().map(CriteriaToElasticsearchConverter::convertQuery).toList();
				yield BoolQuery.of(b -> b.should(queryList))._toQuery();
			}
			case NotFilterOperand o -> BoolQuery.of(b -> b.mustNot(convertQuery(o.operands().getFirst())))._toQuery();
			default -> convertQuery(operand);
		};
	}

	private static Query convertQuery(final FilterOperand operand) {
		return switch (operand) {
			case ContainsOperand(String field, Object value) -> {
				String wildcardValue = "*" + value.toString() + "*";
				yield WildcardQuery.of(w -> w.field(field).value(wildcardValue).caseInsensitive(true))._toQuery();
			}
			case ContainsStrictOperand(String field, Object value) -> {
				String wildcardValue = "*" + value.toString() + "*";
				yield WildcardQuery.of(w -> w.field(field).value(wildcardValue))._toQuery();
			}
			case EqualsOperand(String field, Object value) -> {
				JsonData jsonDataValue = JsonData.of(value);
				yield TermQuery.of(t -> t.field(field).value(FieldValue.of(jsonDataValue)).caseInsensitive(true))._toQuery();
			}
			case EqualsStrictOperand(String field, Object value) -> {
				JsonData jsonDataValue = JsonData.of(value);
				yield TermQuery.of(t -> t.field(field).value(FieldValue.of(jsonDataValue)))._toQuery();
			}
			case GreaterThanEqualToOperand(String field, Object value) -> {
				JsonData jsonDataValue = JsonData.of(value);
				yield RangeQuery.of(r -> r.untyped(n -> n.field(field).gte(jsonDataValue)))._toQuery();
			}
			case GreaterThanOperand(String field, Object value) -> {
				JsonData jsonDataValue = JsonData.of(value);
				yield RangeQuery.of(r -> r.untyped(n -> n.field(field).gt(jsonDataValue)))._toQuery();
			}
			case LowerThanEqualOperand(String field, Object value) -> {
				JsonData jsonDataValue = JsonData.of(value);
				yield RangeQuery.of(r -> r.untyped(n -> n.field(field).lte(jsonDataValue)))._toQuery();
			}
			case LowerThanOperand(String field, Object value) -> {
				JsonData jsonDataValue = JsonData.of(value);
				yield RangeQuery.of(r -> r.untyped(n -> n.field(field).lt(jsonDataValue)))._toQuery();
			}
			case NotEqualsOperand(String field, Object value) -> {
				FieldValue fieldValue = getFieldValue(value);
				yield BoolQuery.of(b -> b.mustNot(m -> m.term(t -> t.field(field).value(fieldValue).caseInsensitive(true))))._toQuery();
			}
			case NotEqualsStrictOperand(String field, Object value) -> {
				FieldValue fieldValue = getFieldValue(value);
				yield BoolQuery.of(b -> b.mustNot(m -> m.term(t -> t.field(field).value(fieldValue))))._toQuery();
			}
			case RegExpOperand(String field, Object value) -> {
				String regexValue = (String) value;
				yield RegexpQuery.of(re -> re.field(field).value(regexValue))._toQuery();
			}
			default -> filterOperandToQuery(operand);
		};
	}

	private static FieldValue getFieldValue(final Object s) {
		return switch (s) {
			case Boolean b -> FieldValue.of(b);
			case Long l -> FieldValue.of(l);
			case Integer i -> FieldValue.of(i);
			case null, default -> FieldValue.of((String) s);
		};
	}

	private static String capitalizeFirstLetter(String input) {
		if (input == null || input.isEmpty()) {
			return input;
		}
		return input.substring(0, 1).toUpperCase() + input.substring(1);
	}

	private static String searchRequestToElasticsearchQueryJsonDsl(final SearchRequest searchRequest) {
		JsonpMapper   jsonpMapper = new JacksonJsonpMapper();
		StringWriter  writer      = new StringWriter();
		JsonGenerator generator   = jsonpMapper.jsonProvider().createGenerator(writer);
		searchRequest.serialize(generator, jsonpMapper);
		generator.close();
		return writer.toString();
	}

}
