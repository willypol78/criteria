package com.codely.criteria.from;

import com.codely.criteria.Criteria;
import com.codely.criteria.errors.InvalidCriteria;
import com.codely.criteria.filters.FilterPrimitives;
import com.codely.criteria.from.transformers.FilterPrimitivesMapper;
import com.fasterxml.jackson.databind.JavaType;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class CriteriaFromHttpServletRequestDqlConverter {
	private static final Logger log = LogManager.getLogger(CriteriaFromHttpServletRequestDqlConverter.class);

	public Criteria toCriteria(HttpServletRequest postRequest) throws InvalidCriteria, InvalidDQL {

		List<FilterPrimitives> filters = toFilterPrimitivesListFromBody(postRequest);

		Integer pageSize   = Optional.ofNullable(postRequest.getParameter("size")).map(Integer::parseInt).orElse(null);
		Integer pageNumber = Optional.ofNullable(postRequest.getParameter("page")).map(Integer::parseInt).orElse(null);
		String  orderBy    = postRequest.getParameter("orderBy");
		String  orderType  = postRequest.getParameter("orderType");

		return Criteria.fromPrimitives(filters, orderBy, orderType, pageSize, pageNumber);
	}

	private List<FilterPrimitives> toFilterPrimitivesListFromBody(HttpServletRequest request) throws InvalidDQL {

		FilterPrimitivesMapper mapper = new FilterPrimitivesMapper();

		try (BufferedReader reader = request.getReader()) {
			String   body = reader.lines().collect(Collectors.joining(System.lineSeparator()));
			JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, FilterPrimitives.class);
			return mapper.readValue(body, type);
		} catch (IOException e) {
			log.error("Error reading request body");
			log.trace(e);
			throw new InvalidDQL();
		}
	}
}
