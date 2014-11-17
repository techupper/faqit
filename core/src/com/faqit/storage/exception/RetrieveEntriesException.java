package com.faqit.storage.exception;

public class RetrieveEntriesException extends Exception {
	private static final long serialVersionUID = 1L;

	public RetrieveEntriesException(String message) {
		super("Could not retrieve top entries: " + message);
	}

}
