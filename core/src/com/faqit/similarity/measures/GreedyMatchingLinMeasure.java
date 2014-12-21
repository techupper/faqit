package com.faqit.similarity.measures;

import semilar.data.Sentence;
import semilar.sentencemetrics.GreedyComparer;
import semilar.wordmetrics.WNWordMetric;
import semilar.tools.semantic.WordNetSimilarity;

import com.faqit.similarity.TextToolkit;
import com.faqit.similarity.measures.exception.SimilarityMeasureException;

public class GreedyMatchingLinMeasure extends SimilarityMeasure {
	GreedyComparer greedyComparerWNLin;//greedy matching, use wordnet LIN method for Word 2 Word similarity
	WNWordMetric wnMetricLin;

	public GreedyMatchingLinMeasure(Float weight) {
		super(weight);
		wnMetricLin = new WNWordMetric(WordNetSimilarity.WNSimMeasure.LIN, false);
		greedyComparerWNLin = new GreedyComparer(wnMetricLin, 0.3f, false);
	}

	@Override
	public Float score(String t1, String t2) throws SimilarityMeasureException {
		Sentence s1 = TextToolkit.getPreprocessor().preprocessSentence(t1);
		Sentence s2 = TextToolkit.getPreprocessor().preprocessSentence(t2);
		return new Float(greedyComparerWNLin.computeSimilarity(s1, s2));
	}

}
