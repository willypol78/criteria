package io.github.willypol78.criteria.to;

import java.io.Serializable;
import java.util.Map;

public record SqlAndParams(String sql, Map<String, Serializable> params) {}
