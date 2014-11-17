package com.faqit.similarity.measures.exception;

public class SimilarityMeasureException extends Exception{
	private static final long serialVersionUID = 1L;

	public SimilarityMeasureException() {
		super();
	}

	public SimilarityMeasureException(String message) {
		super(message);
	}

	public SimilarityMeasureException(String message, Throwable cause) {
		super(message, cause);
	}

	public SimilarityMeasureException(Throwable cause) {
		super(cause);
	}
}
