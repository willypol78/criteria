package dev.willypol.to;

public interface SQLTransformer {
	String limitAndOffset(Integer pageNumber, Integer size);
}
