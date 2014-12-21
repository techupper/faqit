package com.faqit.similarity.measures;

import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.ChapmanLengthDeviation;

import com.faqit.similarity.measures.exception.SimilarityMeasureException;

public class ChapmanLengthDeviationMeasure extends SimilarityMeasure {
	private AbstractStringMetric metric;

	public ChapmanLengthDeviationMeasure(Float weight) {
		super(weight);
		metric = new ChapmanLengthDeviation();
	}

	@Override
	public Float score(String t1, String t2) throws SimilarityMeasureException {
		return metric.getSimilarity(t1, t2);
	}

}
