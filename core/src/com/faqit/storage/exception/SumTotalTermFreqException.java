package com.faqit.storage.exception;

public class SumTotalTermFreqException extends Exception {
	private static final long serialVersionUID = 1L;

	public SumTotalTermFreqException() {
		super("Could not retrieve the sum of the total term frequencies.");
	}

}