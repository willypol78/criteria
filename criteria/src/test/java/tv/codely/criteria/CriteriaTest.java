package tv.codely.criteria;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tv.codely.criteria.errors.InvalidCriteria;
import tv.codely.criteria.filters.Filters;
import tv.codely.criteria.order.Order;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;


class CriteriaTest {

	@Test
	@DisplayName("Should throw an error when the page number is defined but page size is not")
	void shouldThrowAnErrorWhenThePageNumberIsDefinedButPageFilterSizeIsNot() {
		assertThrows(InvalidCriteria.class, () -> {
			new Criteria(new Filters(List.of()), Order.none(), null, new Random().nextInt(100) + 1);
		});
	}

}
