package io.github.willypol78.criteria.to;

public interface SQLTransformer {
	String limitAndOffset(Integer pageNumber, Integer size);
}
