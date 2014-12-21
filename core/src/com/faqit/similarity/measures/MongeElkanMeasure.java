package com.faqit.similarity.measures;

import uk.ac.shef.wit.simmetrics.similaritymetrics.*;

import com.faqit.similarity.measures.exception.SimilarityMeasureException;

public class MongeElkanMeasure extends SimilarityMeasure {
	private AbstractStringMetric metric;

	public MongeElkanMeasure(Float weight) {
		super(weight);
		metric = new MongeElkan();
	}

	@Override
	public Float score(String t1, String t2) throws SimilarityMeasureException {
		return metric.getSimilarity(t1, t2);
	}

}
