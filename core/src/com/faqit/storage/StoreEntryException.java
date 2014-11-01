package com.faqit.storage;

public class StoreEntryException extends Exception {
	private static final long serialVersionUID = 1L;

	public StoreEntryException() {
		super();
	}

	public StoreEntryException(String message) {
		super(message);
	}

	public StoreEntryException(String message, Throwable cause) {
		super(message, cause);
	}

	public StoreEntryException(Throwable cause) {
		super(cause);
	}
}
