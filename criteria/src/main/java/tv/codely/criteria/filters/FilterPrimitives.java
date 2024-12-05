package tv.codely.criteria.filters;

import java.util.Optional;

public record FilterPrimitives(Optional<String> field, String operator, Optional<String> value) {}
