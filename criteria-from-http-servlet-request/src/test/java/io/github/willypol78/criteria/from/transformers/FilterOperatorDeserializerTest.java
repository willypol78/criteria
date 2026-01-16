package io.github.willypol78.criteria.from.transformers;

import io.github.willypol78.criteria.operators.BooleanFilterOperator;
import io.github.willypol78.criteria.operators.FilterOperator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FilterOperatorDeserializerTest {
	private FilterOperatorDeserializer deserializer;

	@Mock
	private JsonParser jsonParser;

	@Mock
	private DeserializationContext deserializationContext;

	@Mock
	private JsonNode jsonNode;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		deserializer = new FilterOperatorDeserializer();
	}

	@Test
	@DisplayName("Should deserialize and return an instance of BooleanFilterOperator AND")
	void shouldDeserializeAndReturnAnInstanceOfBooleanFilterOperatorAND() throws IOException {
		// Configuración para un operador válido (por ejemplo, "AND")
		when(jsonParser.getCodec()).thenReturn(mock(ObjectCodec.class));
		when(jsonParser.getCodec().readTree(jsonParser)).thenReturn(jsonNode);
		when(jsonNode.asText()).thenReturn("AND");

		// Ejecución
		FilterOperator result = deserializer.deserialize(jsonParser, deserializationContext);

		// Verificaciones
		assertNotNull(result);
		assertEquals(BooleanFilterOperator.AND, result);
	}

	@Test
	@DisplayName("Should throw IOException for invalid operator")
	void shouldThrowIOExceptionForInvalidOperator() {
		// Configuración para un operador inválido
		try {
			when(jsonParser.getCodec()).thenReturn(mock(ObjectCodec.class));
			when(jsonParser.getCodec().readTree(jsonParser)).thenReturn(jsonNode);
			when(jsonNode.asText()).thenReturn("INVALID_OPERATOR");

			assertThrows(IOException.class, () -> {
				deserializer.deserialize(jsonParser, deserializationContext);
			});

		} catch (IOException e) {
			fail("Unexpected exception: " + e.getMessage());
		}
	}

	@Test
	@DisplayName("Should throw IOException for null or empty operator")
	void shouldThrowIOExceptionForNullOrEmptyOperator() {
		// Configuración para un operador nulo o vacío
		try {
			when(jsonParser.getCodec()).thenReturn(mock(ObjectCodec.class));
			when(jsonParser.getCodec().readTree(jsonParser)).thenReturn(jsonNode);
			when(jsonNode.asText()).thenReturn(null);

			// Ejecución y verificación
			Exception exception = assertThrows(IOException.class, () -> {
				deserializer.deserialize(jsonParser, deserializationContext);
			});

			assertEquals(IOException.class, exception.getClass());
		} catch (IOException e) {
			fail("Unexpected exception: " + e.getMessage());
		}
	}
}
