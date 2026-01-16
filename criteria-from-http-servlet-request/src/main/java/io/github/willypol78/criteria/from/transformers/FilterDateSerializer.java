package io.github.willypol78.criteria.from.transformers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class FilterDateSerializer extends StdSerializer<Date> {

	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	public FilterDateSerializer() {
		this(null);
	}

	public FilterDateSerializer(Class<Date> t) {
		super(t);
	}

	@Override
	public void serialize(Date value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
		jsonGenerator.writeString(sdf.format(value));
		jsonGenerator.close();
	}
}
