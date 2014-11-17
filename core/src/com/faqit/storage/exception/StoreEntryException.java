package com.faqit.storage.exception;

public class StoreEntryException extends Exception {
	private static final long serialVersionUID = 1L;

	public StoreEntryException(String entryId) {
		super("Failed to store the entry with id=" + entryId + ".");
	}

}
