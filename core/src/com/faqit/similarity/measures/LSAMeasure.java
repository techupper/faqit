package com.faqit.similarity.measures;

import com.faqit.similarity.measures.exception.SimilarityMeasureException;

import semilar.config.ConfigManager;
import semilar.data.Sentence;
import semilar.sentencemetrics.LSAComparer;
import semilar.tools.preprocessing.SentencePreprocessor;

public class LSAMeasure extends SimilarityMeasure {
	private static final String SEMILAR_DATA_PATH = "./external libs/semilar/";
	//private static final String WORDNET_PATH = "./external libs/semilar/WordNet-JWI";
	
	private LSAComparer lsaComparer = null;
	private SentencePreprocessor preprocessor = null;

	public LSAMeasure(Float weight) {
		super(weight);
		ConfigManager.setSemilarHomeFolder(SEMILAR_DATA_PATH);
		ConfigManager.setSemilarDataRootFolder(SEMILAR_DATA_PATH);
		
		// lsaComparer: This is different from lsaMetricTasa, as this method
		// will
		// directly calculate sentence level similarity whereas lsaMetricTasa
		// is a word 2 word similarity metric used with Optimum and Greedy
		// methods.
		lsaComparer = new LSAComparer("LSA-MODEL-TASA-LEMMATIZED-DIM300");
		//lsaComparer = new LSAComparer("LSA-MODEL-WIKI-6");
		preprocessor = new SentencePreprocessor(
				SentencePreprocessor.TokenizerType.STANFORD,
				SentencePreprocessor.TaggerType.STANFORD,
				SentencePreprocessor.StemmerType.STANFORD,
				SentencePreprocessor.ParserType.STANFORD);
	}

	@Override
	public Float score(String t1, String t2) throws SimilarityMeasureException {
		Sentence s1 = preprocessor.preprocessSentence(t1);
		Sentence s2 = preprocessor.preprocessSentence(t2);
		Float result = new Float(lsaComparer.computeSimilarity(s1, s2));
		return result.isNaN() ? 0f : result * getWeight();
	}

}
