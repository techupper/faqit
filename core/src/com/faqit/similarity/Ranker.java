package com.faqit.similarity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import uk.ac.shef.wit.simmetrics.similaritymetrics.ChapmanLengthDeviation;
import uk.ac.shef.wit.simmetrics.similaritymetrics.JaroWinkler;

import com.faqit.similarity.exception.RankerGeneralException;
import com.faqit.similarity.measures.*;
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
	// private static boolean l2r = false;

	private static List<SimilarityMeasure> measures;

	private Ranker() {
		measures = new ArrayList<SimilarityMeasure>();
		// TODO should we make this parameterized by using an xml config file?
		//SimilarityMeasure sm1 = new NGramOverlapMeasure(1f);
		//measures.add(sm1);
		//SimilarityMeasure sm2 = new LSAMeasure(1f);
		//measures.add(sm2);
		//SimilarityMeasure sm3 = new ICWeightedOverlapMeasure(1f);
		//measures.add(sm3);
		//SimilarityMeasure sm4 = new AlignedLemmaOverlapMeasure(1f);
		//measures.add(sm4);
		SimilarityMeasure sm5 = new SimonWhiteMeasure(0.8f);
		measures.add(sm5);
		//SimilarityMeasure sm6 = new GreedyMatchingLinMeasure(1f);
		//measures.add(sm6);
		SimilarityMeasure sm7 = new MongeElkanMeasure(0.2f);
		measures.add(sm7);
		//SimilarityMeasure sm8 = new ChapmanLengthDeviationMeasure(1f);
		//measures.add(sm8);
	}

	public static Ranker getInstance() {
		return INSTANCE;
	}
	
	public static int getNumFeatures(){
		return measures.size();
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
		String result = "I do not have enough information to answer your question.";
		try {
			// 2. Wait for user query and retrieve N most similar entries based
			// on IF-IDF
			List<Entry> topEntries = storageManager.RetrieveTopEntries(query);

			// 3. Apply similarity measures to rank retrieved documents
			if (!measures.isEmpty()) {
				for (Entry entry : topEntries) {
					for (SimilarityMeasure sm : measures) {
						if (sm.getWeight() == 0f)
							continue;
						entry.setScore(entry.getScore()
								//+ (sm.score(entry.getAnswer(), query)
								//* ANSWER_WEIGHT
								+ (sm.score(TextToolkit.tokenizeNRemoveStopWords(entry.getQuestion()), TextToolkit.tokenizeNRemoveStopWords(query))
								* QUESTION_WEIGHT)*sm.getWeight());
					}
				}
			}
			// 4. Apply Learning to rank algorithm to learn weights
			rank(topEntries);

			// 5. Return the top 1 answer for the most similar question
			if (topEntries.size() > 0) {
				result = topEntries.get(0).getAnswer();
			}

			if (debug) {
				dumpEntries(query, topEntries);
			}

		} catch (RetrieveEntriesException | SimilarityMeasureException | IOException e) {
			throw new RankerGeneralException(STANDARD_EXCEPTION_MSG
					+ e.getMessage());
		}

		return result;
	}

	public static Storage getStorageManager() {
		return storageManager;
	}
	
	public static Float getFeature(String st1, String st2, int numFeature) throws SimilarityMeasureException, IOException{
		return measures.get(numFeature - 1).score(TextToolkit.tokenizeNRemoveStopWords(st1), TextToolkit.tokenizeNRemoveStopWords(st2));
	}

	// TODO to use a learning to rank approach
	private static void rank(List<Entry> topEntries) {
		// if(l2r != true){
		Collections.sort(topEntries);
		/*
		 * }else{ List<RankList> samples = new ArrayList();
		 * 
		 * RankerFactory rankerFactory = new RankerFactory();
		 * ciir.umass.edu.learning.Ranker l2rRanker =
		 * rankerFactory.loadRanker(L2R_MODEL_PATH);
		 * 
		 * //para cada faq possivel do topEntries DataPoint qp = new
		 * DataPoint("feature vector: 3 qid:1 1:1 2:1 3:0 4:0.2 5:0 # 1A");
		 * RankList rl = new RankList(); samples.add(rl); rl.add(qp);
		 * 
		 * //para cada sample do samples }
		 */
	}

	private static void dumpEntries(String query, List<Entry> topEntries) {
		String storageResults = "Results: ORDER (SCORE) : QUESTION \n\n";

		if (topEntries.isEmpty()) {
			storageResults += "<No similar entries for " + query + ">";
		} else {
			int i = 1;
			for (Entry e : topEntries) {
				//storageResults += "\t" + i + ": " + "score = " + e.getScore()
				//		+ " : " + " ifidf = " + e.getBaseScore() + " : "
				//		+ e.getQuestion() + "\n";
				storageResults += i + "(" + e.getScore() + ") : " + e.getQuestion() + "\n" ;
				i++;
			}
		}
		System.out.println(storageResults + "\n");
	}

}
