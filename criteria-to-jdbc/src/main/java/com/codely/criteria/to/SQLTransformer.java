package com.codely.criteria.to;

public interface SQLTransformer {
	String limitAndOffset(Integer pageNumber, Integer size);
}
