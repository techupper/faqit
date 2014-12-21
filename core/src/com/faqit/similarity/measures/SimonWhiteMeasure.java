/*
 * Simon White or Dice's Coefficient algorithm
 * 
 * Credits of this algorithm go to Simon White who has described a Java implementation here:
 * http://www.catalysoft.com/articles/StrikeAMatch.html
 */

package com.faqit.similarity.measures;

import java.util.ArrayList;

import com.faqit.similarity.measures.exception.SimilarityMeasureException;

public class SimonWhiteMeasure extends SimilarityMeasure {

	public SimonWhiteMeasure(Float weight) {
		super(weight);
	}

	/** @return an ArrayList of 2-character Strings. */
	private static ArrayList wordLetterPairs(String str) {
		ArrayList allPairs = new ArrayList();
		// Tokenize the string and put the tokens/words into an array
		String[] words = str.split("\\s");

		// For each word
		for (int w = 0; w < words.length; w++) {

			// Find the pairs of characters
			String[] pairsInWord = letterPairs(words[w]);
			for (int p = 0; p < pairsInWord.length; p++) {
				allPairs.add(pairsInWord[p]);
			}
		}
		return allPairs;
	}

	private static String[] letterPairs(String str) {
		int numPairs = str.length() - 1;
		
		if(numPairs < 1) 
			return new String[0];
		
		String[] pairs = new String[numPairs];

		for (int i = 0; i < numPairs; i++) {
			pairs[i] = str.substring(i, i + 2);
		}
		
		return pairs;
	}

	/** @return lexical similarity value in the range [0,1] */
	@Override
	public Float score(String t1, String t2) throws SimilarityMeasureException {
		ArrayList pairs1 = wordLetterPairs(t1.toUpperCase());
		ArrayList pairs2 = wordLetterPairs(t2.toUpperCase());

		int intersection = 0;
		int union = pairs1.size() + pairs2.size();

		for (int i = 0; i < pairs1.size(); i++) {
			Object pair1 = pairs1.get(i);
			for (int j = 0; j < pairs2.size(); j++) {
				Object pair2 = pairs2.get(j);
				if (pair1.equals(pair2)) {
					intersection++;
					pairs2.remove(j);
					break;
				}
			}
		}
		return (float) ((2.0 * intersection) / union);
	}

}
