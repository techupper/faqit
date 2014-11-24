package com.faqit.similarity.measures;

import java.util.ArrayList;
import java.util.List;

import semilar.data.Sentence;
import semilar.data.Word;
import semilar.wordmetrics.LSAWordMetric;

import com.faqit.similarity.TextToolkit;
import com.faqit.similarity.measures.exception.SimilarityMeasureException;

public class AlignedLemmaOverlapMeasure extends SimilarityMeasure {

	private LSAWordMetric lsaMetricTasa;

	public AlignedLemmaOverlapMeasure(Float weight) {
		super(weight);

		// semilar related
		lsaMetricTasa = new LSAWordMetric(TextToolkit.getLsaModelPath());
	}

	@Override
	public Float score(String t1, String t2) throws SimilarityMeasureException {
		return computeALO(t1, t2);
	}

	private class WordPair {
		public String w1;
		public String w2;
		public Float lsaSim;

		public WordPair(String w1, String w2, Float lsaSim) {
			this.w1 = w1;
			this.w2 = w2;
			this.lsaSim = lsaSim;
		}
	}

	private Float computeALO(String t1, String t2) {
		List<WordPair> pairs = extractBestPairsOfWords(t1, t2);

		Float numerator = computePairsSum(pairs);
		Float denominator = (float) Math.max(t1.length(), t2.length());

		return numerator / denominator;
	}

	private List<WordPair> extractBestPairsOfWords(String t1, String t2) {
		List<WordPair> pairs = new ArrayList<WordPair>();

		Sentence sentence = TextToolkit.getPreprocessor().preprocessSentence(
				TextToolkit.getTokenizedString(t1 + " " + t2));

		ArrayList<Word> words = sentence.getWords();
		Word[] wordsArray = new Word[words.size()];
		words.toArray(wordsArray);

		Float previousBestSSIM = 0f;
		Word previousBestWordPair = null;
		Float tempSSIM = 0f;
		int w2Index = 0;
		for (int j = 0; j < wordsArray.length; j++) {
			if (wordsArray[j] == null) {
				continue;
			}
			for (int i = j + 1; i < wordsArray.length; i++) {
				if (wordsArray[i] == null) {
					continue;
				} else {
					tempSSIM = computeSSIM(wordsArray[j].getRawForm(),
							wordsArray[i].getRawForm());
					if (tempSSIM > previousBestSSIM) {
						if (previousBestWordPair != null) {
							wordsArray[w2Index] = previousBestWordPair; // replace
																		// the
																		// previous
																		// pair
																		// taken
						}
						w2Index = i; // remember the index of the best pair so
										// far
						previousBestWordPair = wordsArray[i]; // and the
																// corresponding
																// best word
																// pair
						wordsArray[i] = null; // reserve the new best pair
						previousBestSSIM = tempSSIM;
					}
				}
			}
			pairs.add(new WordPair(wordsArray[j].getRawForm(),
					wordsArray[w2Index] == null ? "" : wordsArray[w2Index]
							.getRawForm(), previousBestSSIM));
		}

		/*
		 * for (WordPair wp : pairs) { System.out.println("PAIR : " + "\t\t" +
		 * wp.w1 + "\t\t" + wp.w2 + "\t\t SCORE=" + wp.lsaSim); }
		 */

		return pairs;
	}

	private Float computePairsSum(List<WordPair> pairs) {
		Float sum = 0f;
		for (WordPair pair : pairs) {
			sum += computeSIM(pair);
		}
		return sum;
	}

	/**
	 * @return LSA similarity between w1 and w2
	 */
	private Float computeSSIM(String w1, String w2) {
		return (float) lsaMetricTasa.computeWordSimilarityNoPos(w1, w2);
	}

	private Float computeSIM(WordPair pair) {
		// TODO computeIC should be placed in storage. the current call is just
		// a stub
		Float a1 = TextToolkit.computeIC(pair.w1);
		Float a2 = TextToolkit.computeIC(pair.w2) * pair.lsaSim;
		return Math.max(a1, a2);
	}

}
