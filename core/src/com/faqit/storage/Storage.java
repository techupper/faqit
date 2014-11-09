package com.faqit.storage;

import java.util.List;

public interface Storage {
	void storeEntry(Entry entry) throws StoreEntryException;

	List<Entry> RetrieveTopEntries(String userInput)
			throws RetrieveEntriesException;
}
