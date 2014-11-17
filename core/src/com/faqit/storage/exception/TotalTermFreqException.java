package com.faqit.storage.exception;

public class TotalTermFreqException extends Exception {
	private static final long serialVersionUID = 1L;

	public TotalTermFreqException() {
		super("Could not retrieve the total term frequency.");
	}

}
