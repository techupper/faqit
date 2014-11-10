package com.faqit.storage;

import java.util.List;

public interface Storage {
	public static final int NUMBER_OF_HITS = 10;
	
	void storeEntry(Entry entry) throws StoreEntryException;

	List<Entry> RetrieveTopEntries(String userInput)
			throws RetrieveEntriesException;
}
