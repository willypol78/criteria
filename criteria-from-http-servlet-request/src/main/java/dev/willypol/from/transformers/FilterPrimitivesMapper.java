package dev.willypol.from.transformers;


import dev.willypol.operators.FilterOperator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Date;

public class FilterPrimitivesMapper extends ObjectMapper {

	public FilterPrimitivesMapper() {
		super();
		SimpleModule simpleModule = new SimpleModule();
		simpleModule.addSerializer(Date.class, new FilterDateSerializer());
		simpleModule.addDeserializer(FilterOperator.class, new FilterOperatorDeserializer());
		this.registerModule(new JavaTimeModule());
		this.registerModule(new Jdk8Module());
		this.registerModule(simpleModule);
		this.enable(SerializationFeature.WRITE_ENUMS_USING_INDEX);
	}
}
