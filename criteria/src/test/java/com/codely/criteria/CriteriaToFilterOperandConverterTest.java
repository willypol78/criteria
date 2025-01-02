package com.codely.criteria;

import com.codely.criteria.errors.InvalidCriteria;
import com.codely.criteria.errors.InvalidFilter;
import com.codely.criteria.filters.Filters;
import com.codely.criteria.operands.FilterOperand;
import com.codely.criteria.operands.logical.VoidFilterOperand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
		assertTrue(filterOperand instanceof VoidFilterOperand);
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
