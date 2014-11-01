package com.faqit.storage;

public interface Storage {
	
	void StoreEntry();
	Entry[] RetrieveTopEntries();
	//implementation -> lucene 
}
