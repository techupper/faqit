package com.faqit.storage;

import java.io.IOException;

public interface StorageInterface {
	void StoreEntry(Entry entry) throws StoreEntryException;
	Entry[] RetrieveTopEntries(String userInput) throws RetrieveEntriesException;
}
