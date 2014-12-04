package com.faqit.similarity.measures;

import semilar.config.ConfigManager;
import semilar.data.Sentence;
import semilar.sentencemetrics.LSAComparer;

import com.faqit.similarity.TextToolkit;
import com.faqit.similarity.measures.exception.SimilarityMeasureException;

public class LSAMeasure extends SimilarityMeasure {
	
	private LSAComparer lsaComparer = null;

	public LSAMeasure(Float weight) {
		super(weight);
		
		// semilar related
		lsaComparer = new LSAComparer(TextToolkit.getLsaModelPath());
	}

	@Override
	public Float score(String t1, String t2) throws SimilarityMeasureException {
		Sentence s1 = TextToolkit.getPreprocessor().preprocessSentence(t1);
		Sentence s2 = TextToolkit.getPreprocessor().preprocessSentence(t2);
		Float result = new Float(lsaComparer.computeSimilarity(s1, s2));
		return result.isNaN() ? 0f : result;
	}

}
