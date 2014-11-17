package com.faqit.similarity.exception;

public class RankerGeneralException extends Exception{
	private static final long serialVersionUID = 1L;

	public RankerGeneralException() {
		super();
	}

	public RankerGeneralException(String message) {
		super(message);
	}

	public RankerGeneralException(String message, Throwable cause) {
		super(message, cause);
	}

	public RankerGeneralException(Throwable cause) {
		super(cause);
	}
}