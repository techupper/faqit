package com.faqit.storage;

import java.io.IOException;

public interface Storage {
	void storeEntry(Entry entry) throws StoreEntryException;

	Entry[] RetrieveTopEntries(String userInput)
			throws RetrieveEntriesException;
}
