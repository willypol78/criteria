package dev.willypol.from.transformers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

class FilterDateSerializerTest {


	@Test
	@DisplayName("Should serialize date correctly")
	void shouldSerializeDateCorrectly() throws IOException, ParseException {
		// Given
		Date          date          = new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-01");
		StringWriter  writer        = new StringWriter();
		JsonGenerator jsonGenerator = mock(JsonGenerator.class);
		doAnswer(invocation -> {
			writer.write((String) invocation.getArgument(0));
			return null;
		}).when(jsonGenerator).writeString(anyString());

		SerializerProvider serializerProvider = mock(SerializerProvider.class);

		FilterDateSerializer serializer = new FilterDateSerializer();

		// When
		serializer.serialize(date, jsonGenerator, serializerProvider);

		// Then
		String expectedDateString = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(date);
		assertEquals(expectedDateString, writer.toString());
	}
}
