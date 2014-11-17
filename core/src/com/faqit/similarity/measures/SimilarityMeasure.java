package com.faqit.similarity.measures;

import com.faqit.similarity.measures.exception.SimilarityMeasureException;


public abstract class SimilarityMeasure {
	private Float weight = 0f;
	
	public SimilarityMeasure(Float weight){
		this.weight = weight;
	}
	
	public abstract Float score(String t1, String t2) throws SimilarityMeasureException;
	
	public Float getWeight() {
		return weight;
	}
}
