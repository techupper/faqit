package com.faqit.similarity.measures;

import java.util.ArrayList;
import java.util.List;

import semilar.config.ConfigManager;
import semilar.wordmetrics.LSAWordMetric;

import com.faqit.similarity.TextToolkit;
import com.faqit.similarity.measures.exception.SimilarityMeasureException;

public class AlignedLemmaOverlapMeasure extends SimilarityMeasure {
	private static final String SEMILAR_DATA_PATH = "./external libs/semilar/";
	private LSAWordMetric lsaMetricTasa;

	public AlignedLemmaOverlapMeasure(Float weight) {
		super(weight);
		ConfigManager.setSemilarHomeFolder(SEMILAR_DATA_PATH);
		ConfigManager.setSemilarDataRootFolder(SEMILAR_DATA_PATH);
		lsaMetricTasa = new LSAWordMetric("LSA-MODEL-TASA-LEMMATIZED-DIM300");
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

		String[] words1 = t1.split(" ");
		String[] words2 = t2.split(" ");

		Float previousBestSSIM = 0f;
		Float tempSSIM = 0f;
		int w2Index = 0;
		for (String w1 : words1) {
			for (int i = 0; i < words2.length; i++) {
				if (words2[i] == null) {
					continue;
				} else {
					tempSSIM = computeSSIM(w1, words2[i]);
					if (tempSSIM > previousBestSSIM) {
						w2Index = i;
						words2[i] = null;
						previousBestSSIM = tempSSIM;
					}
				}
			}
			pairs.add(new WordPair(w1, words2[w2Index], previousBestSSIM));
		}
		for(WordPair wp : pairs){
			System.out.println("PAIR : " + wp.w1 + "::::" + wp.w2 + ":::: SCORE="+wp.lsaSim);
		}
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
		Float a1 = TextToolkit.computeIC(pair.w1);
		Float a2 = TextToolkit.computeIC(pair.w2) * pair.lsaSim;
		return Math.max(a1, a2);
	}

}
