package dev.willypol.filters;

import java.util.Optional;

public record FilterPrimitives(Optional<String> field, String operator, Optional<String> value) {}
