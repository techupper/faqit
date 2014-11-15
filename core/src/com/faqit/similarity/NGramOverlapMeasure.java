package com.faqit.similarity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Pattern;

import com.faqit.similarity.NGramExtractor;

public class NGramOverlapMeasure extends SimilarityMeasure {
	private NGramExtractor extractor;

	public NGramOverlapMeasure(Float weight) {
		super(weight);
		extractor = new NGramExtractor();
	}

	public Float score(String t1, String t2) throws SimilarityMeasureException {
		Float result = 0f;
		
		Pattern oneWordPattern = Pattern.compile("^[a-zA-Z0-9_]+$");
		boolean oneWordParameter = false;
		
		try {
			//prepare yourself for the dirtiest puzzled code
			if(!t1.isEmpty()){
				result += coverage(t1, t2, 1) * 0.5f;				
			}else{
				oneWordParameter = true;
			}
			if(!oneWordPattern.matcher(t2).find()){					
				result += coverage(t1, t2, 2) * (oneWordParameter ? 1f : 0.5f);
			}else{
				if(oneWordParameter){
					throw new IOException(); //yet another elegant escape :D
				}else{
					result += result;
				}
			}
		} catch (IOException e) {
			throw new SimilarityMeasureException(
					"An error occurred while applying the ngram overlap similarity measure.");
		}

		return result * getWeight();
	}

	private Float coverage(String t1, String t2, int ngram) throws FileNotFoundException, IOException {
		
		extractor.extract(t1, ngram);
		LinkedList<String> uniqueNGramsT1 = extractor.getUniqueNGrams();
		int numNGramsT1 = uniqueNGramsT1.size();
		extractor.extract(t2, ngram);
		LinkedList<String> uniqueNGramsT2 = extractor.getUniqueNGrams();
		int numNGramsT2 = uniqueNGramsT2.size();

		int commonNGrams = 0;
		for (String ng : uniqueNGramsT1) {
			if (uniqueNGramsT2.contains(ng)) {
				commonNGrams++;
			}
		}
		return (0.5f * (commonNGrams / numNGramsT1))
				+ (0.5f * (commonNGrams / numNGramsT2));
	}
}
