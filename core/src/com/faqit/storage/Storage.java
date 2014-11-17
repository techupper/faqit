package com.faqit.storage;

import java.util.List;

import com.faqit.storage.exception.RetrieveEntriesException;
import com.faqit.storage.exception.StoreEntryException;
import com.faqit.storage.exception.SumTotalTermFreqException;
import com.faqit.storage.exception.TotalTermFreqException;

public interface Storage {
	public static final int NUMBER_OF_HITS = 5;

	void storeEntry(Entry entry) throws StoreEntryException;

	List<Entry> RetrieveTopEntries(String userInput) throws RetrieveEntriesException;

	Long getSumTotalTermFreq() throws SumTotalTermFreqException;

	Long getTotalTermFreq(String termText) throws TotalTermFreqException;
}
