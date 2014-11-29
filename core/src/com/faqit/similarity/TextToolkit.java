package com.faqit.similarity;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;

import com.faqit.storage.exception.SumTotalTermFreqException;
import com.faqit.storage.exception.TotalTermFreqException;

import semilar.config.ConfigManager;
import semilar.tools.preprocessing.SentencePreprocessor;

public final class TextToolkit {

	private static final String SEMILAR_DATA_PATH = "./external libs/semilar/";
	// private static final String WORDNET_PATH =
	// "./external libs/semilar/WordNet-JWI";
	private static final String LSA_MODEL_PATH = "TASA-LEMMATIZED-DIM300";
	// we are just interested in the standard preprocessor
	private static final SentencePreprocessor preprocessor = new SentencePreprocessor(
			SentencePreprocessor.TokenizerType.STANFORD,
			SentencePreprocessor.TaggerType.STANFORD,
			SentencePreprocessor.StemmerType.STANFORD,
			SentencePreprocessor.ParserType.STANFORD);
	private static boolean isSemilarOn = false;
	private Long sumTotalTermFreq = null;

	private TextToolkit() {
		try {
			sumTotalTermFreq = Ranker.getStorageManager().getSumTotalTermFreq();
		} catch (SumTotalTermFreqException e) {
			// TODO temporary, remove this
			e.printStackTrace();
		}
	}

	private static void initSemilar() {
		ConfigManager.setSemilarHomeFolder(SEMILAR_DATA_PATH);
		ConfigManager.setSemilarDataRootFolder(SEMILAR_DATA_PATH);
	}

	public static String getLsaModelPath() {
		if(!isSemilarOn){
			initSemilar();
			isSemilarOn = true;
		}
		return LSA_MODEL_PATH;
	}

	public static String tokenizeNRemoveStopWords(String input)
			throws IOException {
		CharArraySet stopWords = EnglishAnalyzer.getDefaultStopSet();
		TokenStream tokenStream = new StandardTokenizer(new StringReader(
				input.trim()));

		tokenStream = new StopFilter(tokenStream, stopWords);
		StringBuilder sb = new StringBuilder();
		CharTermAttribute charTermAttribute = tokenStream
				.addAttribute(CharTermAttribute.class);
		tokenStream.reset();

		while (tokenStream.incrementToken()) {
			String term = charTermAttribute.toString();
			sb.append(term + " ");
		}
		tokenStream.close();
		return sb.toString();
	}

	public static String getTokenizedString(String input) {
		String[] words = input.split("[\\.|\\,|\\?|\\!|\\s]+");

		StringBuilder builder = new StringBuilder();

		for (String s : words) {
			builder.append(s);
			builder.append(" ");
		}
		return builder.toString();
	}

	public static Float computeIC(String s) {
		return 1f;
	}
	
	public Double computeICSum(List<String> list)
			throws TotalTermFreqException {
		Double result = 0d;

		for (String s : list) {
			result += Math.log(sumTotalTermFreq
					/ Ranker.getStorageManager().getTotalTermFreq(s));
		}

		return result;
	}

	public static SentencePreprocessor getPreprocessor() {
		return preprocessor;
	}
}
