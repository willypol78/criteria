package dev.willypol;

import dev.willypol.errors.InvalidCriteria;
import dev.willypol.errors.InvalidFilter;
import dev.willypol.filters.Filters;
import dev.willypol.operands.FilterOperand;
import dev.willypol.operands.logical.VoidFilterOperand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CriteriaToFilterOperandConverterTest {


	@Test
	@DisplayName("Should convert criteria to a void filter operand")
	void shouldConvertCriteriaToVoidFilter() throws InvalidFilter, InvalidCriteria {
		// Given
		Criteria                         criteria  = CriteriaMother.create().build();
		CriteriaToFilterOperandConverter converter = new CriteriaToFilterOperandConverter();

		// When
		FilterOperand filterOperand = converter.convert(criteria);

		// Then
		assertNotNull(filterOperand);
		assertInstanceOf(VoidFilterOperand.class, filterOperand);
	}


	@RepeatedTest(10)
	@DisplayName("Should convert criteria to a filter operand using CriteriaMother")
	void shouldConvertCriteriaToFilterOperand() throws InvalidFilter, InvalidCriteria {
		// Given
		Filters                          filters   = FilterMother.create().random(5).build();
		Criteria                         criteria  = CriteriaMother.create().withFilters(filters).build();
		CriteriaToFilterOperandConverter converter = new CriteriaToFilterOperandConverter();

		// When
		System.out.println(filters.filters());
		FilterOperand filterOperand = converter.convert(criteria);

		// Then
		assertNotNull(filterOperand);
	}
}
