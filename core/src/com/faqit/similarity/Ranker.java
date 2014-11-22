package com.faqit.similarity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import com.faqit.similarity.exception.RankerGeneralException;
import com.faqit.similarity.measures.AlignedLemmaOverlapMeasure;
import com.faqit.similarity.measures.ICWeightedOverlapMeasure;
import com.faqit.similarity.measures.LSAMeasure;
import com.faqit.similarity.measures.NGramOverlapMeasure;
import com.faqit.similarity.measures.SimilarityMeasure;
import com.faqit.similarity.measures.exception.SimilarityMeasureException;
import com.faqit.storage.Entry;
import com.faqit.storage.FAQImporter;
import com.faqit.storage.LuceneStorage;
import com.faqit.storage.Storage;
import com.faqit.storage.exception.RetrieveEntriesException;
import com.faqit.storage.exception.StoreEntryException;

public class Ranker {

	private static final Ranker INSTANCE = new Ranker();
	private static final Float ANSWER_WEIGHT = 0f;
	private static final Float QUESTION_WEIGHT = 1f - ANSWER_WEIGHT;
	private static final String STANDARD_EXCEPTION_MSG = "At the moment it is not possible to process your query: ";

	private static Storage storageManager;
	private static boolean initialized = false;
	private static boolean debug;

	private static List<SimilarityMeasure> measures;

	private Ranker() {
		measures = new ArrayList<SimilarityMeasure>();
		// TODO should we make this parameterized by using an xml config file?
		SimilarityMeasure sm1 = new NGramOverlapMeasure(1f);
		measures.add(sm1);
		//SimilarityMeasure sm2 = new LSAMeasure(1f);
		// measures.add(sm2);
		SimilarityMeasure sm3 = new ICWeightedOverlapMeasure(1f);
		//measures.add(sm3);
		//SimilarityMeasure sm4 = new AlignedLemmaOverlapMeasure(1f);
		//measures.add(sm4);
	}

	public static Ranker getInstance() {
		return INSTANCE;
	}

	public static void init(boolean onDebug) throws RankerGeneralException {
		try {
			storageManager = new LuceneStorage(null);
			debug = onDebug;

			// 1. Start importing FAQs from xml to lucene
			FAQImporter.importFAQ(storageManager);

			initialized = true;
		} catch (FileNotFoundException e) {
			throw new RankerGeneralException(
					"I have lost something, not sure what, but...");
		} catch (IOException e) {
			throw new RankerGeneralException(
					"I feel like there is a wall between us...");
		} catch (StoreEntryException e) {
			throw new RankerGeneralException(
					"I fear I will not remember what you have just told me about...");
		} catch (XMLStreamException e) {
			throw new RankerGeneralException("I cannot see clearly!");
		}
	}

	public static String performQuery(String query)
			throws RankerGeneralException {
		if (!initialized) {
			throw new RankerGeneralException(
					"Ranker was not initialized. Call Ranker.Init()");
		}
		String result = "<Empty>";
		try {
			// 2. Wait for user query and retrieve N most similar entries based
			// on IF-IDF
			List<Entry> topEntries = storageManager.RetrieveTopEntries(query);

			// 3. Apply similarity measures to rank retrieved documents
			if (!measures.isEmpty()) {
				for (Entry entry : topEntries) {
					for (SimilarityMeasure sm : measures) {
						entry.setScore(entry.getScore()
								+ sm.score(entry.getAnswer(), query)
								* ANSWER_WEIGHT
								+ sm.score(entry.getQuestion(), query)
								* QUESTION_WEIGHT);
					}
				}
			}
			// 4. Apply Learning to rank algorithm to learn weights
			rank(topEntries);

			// 5. Return the top 1 similar question
			if (topEntries.size() > 0) {
				result = topEntries.get(0).getQuestion();
			}

			if (debug) {
				dumpEntries(query, topEntries);
			}

		} catch (RetrieveEntriesException | SimilarityMeasureException e) {
			throw new RankerGeneralException(STANDARD_EXCEPTION_MSG
					+ e.getMessage());
		}

		return result;
	}

	public static Storage getStorageManager() {
		return storageManager;
	}

	// TODO to use a learning to rank approach
	private static void rank(List<Entry> topEntries) {
		Collections.sort(topEntries);
	}

	private static void dumpEntries(String query, List<Entry> topEntries) {
		String storageResults = "Results retrieved by Storage: \n";

		if (topEntries.isEmpty()) {
			storageResults += "<No similar entries for " + query + ">";
		} else {
			int i = 1;
			for (Entry e : topEntries) {
				storageResults += "\t" + i + ": " + "score = " + e.getScore()
						+ " : " + e.getQuestion() + "\n";
				i++;
			}
		}
		System.out.println(storageResults + "\n");
	}

}
