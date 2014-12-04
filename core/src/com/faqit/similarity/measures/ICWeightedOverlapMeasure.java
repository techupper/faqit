package com.faqit.similarity.measures;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.faqit.similarity.NGramExtractor;
import com.faqit.similarity.Ranker;
import com.faqit.similarity.measures.exception.SimilarityMeasureException;
import com.faqit.storage.exception.SumTotalTermFreqException;
import com.faqit.storage.exception.TotalTermFreqException;

public class ICWeightedOverlapMeasure extends SimilarityMeasure {
	private NGramExtractor extractor;
	private Long sumTotalTermFreq = null;

	public ICWeightedOverlapMeasure(Float weight) {
		super(weight);
		extractor = new NGramExtractor();
	}

	@Override
	public Float score(String t1, String t2) throws SimilarityMeasureException {
		try {
			sumTotalTermFreq = Ranker.getStorageManager().getSumTotalTermFreq();

			extractor.extract(t1, 1);
			List<String> uniqueNGramsT1 = extractor.getUniqueNGrams();
			extractor.extract(t2, 1);
			List<String> uniqueNGramsT2 = extractor.getUniqueNGrams();

			return ((wwc(uniqueNGramsT1, uniqueNGramsT2) + wwc(uniqueNGramsT2,
					uniqueNGramsT1)) / 2);
		} catch (SumTotalTermFreqException e) {
			throw new SimilarityMeasureException(e);
		} catch (FileNotFoundException e) {
			throw new SimilarityMeasureException(e);
		} catch (IOException e) {
			throw new SimilarityMeasureException(e);
		} catch (TotalTermFreqException e) {
			throw new SimilarityMeasureException(e);
		}
	}

	private Float wwc(List<String> S1, List<String> S2)
			throws TotalTermFreqException {

		return (float) (computeICSum(intersection(S1, S2)) / computeICSum(S2));
	}

	private List<String> intersection(List<String> S1, List<String> S2) {
		List<String> result = new LinkedList<String>(S1);

		result.retainAll(S2);
		return result;
	}

	private Double computeICSum(List<String> list)
			throws TotalTermFreqException {
		Double result = 0d;
		
		for (String s : list) {
			result += Math.log(sumTotalTermFreq
					/ Ranker.getStorageManager().getTotalTermFreq(s));
		}

		return result;
	}

}
