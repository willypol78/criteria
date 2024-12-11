package com.codely.criteria.from.transformers;


import com.codely.criteria.operators.FilterOperator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;

public final class FilterOperatorDeserializer extends StdDeserializer<FilterOperator> {

	private static final Logger log = LogManager.getLogger(FilterOperatorDeserializer.class);

	public FilterOperatorDeserializer() {
		this(null);
	}

	public FilterOperatorDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public FilterOperator deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException {
		JsonNode node = jp.getCodec().readTree(jp);
		return FilterOperator.fromValue(node.asText()).orElseThrow(() -> {
			log.error("Invalid filter operator: {}", node.asText());
			return new IOException();
		});
	}
}
