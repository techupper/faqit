package com.faqit.similarity;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;

public class TextToolkit {
	
	public TextToolkit(){
		
	}
	
	public static String tokenizeNRemoveStopWords(String input) throws IOException{
	    CharArraySet stopWords = EnglishAnalyzer.getDefaultStopSet();
	    TokenStream tokenStream = new StandardTokenizer(new StringReader(input.trim()));
	    
	    tokenStream = new StopFilter(tokenStream, stopWords);
	    StringBuilder sb = new StringBuilder();
	    CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
	    tokenStream.reset();
	    
	    while (tokenStream.incrementToken()) {
	        String term = charTermAttribute.toString();
	        sb.append(term + " ");
	    }
	    tokenStream.close();
	    return sb.toString();
	}
}
