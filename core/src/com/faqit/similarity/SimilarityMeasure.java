package com.faqit.similarity;

abstract class SimilarityMeasure {
	private Float weight = 0f;
	
	public SimilarityMeasure(Float weight){
		this.weight = weight;
	}
	
	public Float score(String t1, String t2) throws SimilarityMeasureException{
		return new Float(0);
	}
	
	public Float getWeight() {
		return weight;
	}
}
