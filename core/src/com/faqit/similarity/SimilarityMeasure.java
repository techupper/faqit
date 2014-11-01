package com.faqit.similarity;

import com.faqit.storage.Entry;

public interface SimilarityMeasure {
	Float score(String query, Entry entry);
}
