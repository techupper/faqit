package com.faqit.similarity;

/*
SimSeer - a search engine for finding similar documents
Copyright (C) 2012-2013  Kyle Williams <kwilliams@psu.edu>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.StringReader;

import java.util.HashMap;
import java.util.LinkedList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.shingle.ShingleAnalyzerWrapper;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

/**
* This is a general class for extracting NGrams from a string of text implemented using Lucene libraries 
* 
* Generally, use is as follows:
* NGramExtractor extrator = new NGramExtractor(); //Initialize a NGramExtractor
* extractor.extract(String text, int nGramSize); //Extracts the n-grams (see overloaded methods for options)
* LinkedList<String> ngrams = extractor.getUniqueNGrams(); //Get the n-grams
*/

public class NGramExtractor {

Analyzer analyzer;

String text = "";
Boolean stopWords = true;
Boolean overlap = false;
int length = 3;

//We store ngrams, unique ngrams, and the frequencies of the ngrams
LinkedList<String> nGrams;
LinkedList<String> uniqueNGrams;
HashMap<String, Integer> nGramFreqs;

/**
 * Default constructor.
 * Initializes the ngram extractor
 */
public NGramExtractor() {

}

/**
 * Extracts NGrams from a String of text.
 * Can handle ngrams of any length and also perform stop word removal before extraction
 * @param text the text that the ngrams should be extracted from
 * @param length the length of the ngrams
 */
public void extract(String text, int length) throws FileNotFoundException, IOException {
    extract(text, length, this.stopWords, this.overlap);
}

/**
 * Extracts NGrams from a String of text.
 * Can handle ngrams of any length and also perform stop word removal before extraction
 * @param text the text that the ngrams should be extracted from
 * @param length the length of the ngrams
 * @param stopWords whether or not stopwords should be removed before extraction
 */
public void extract(String text, int length, Boolean stopWords) throws FileNotFoundException, IOException {
    extract(text, length, stopWords, this.overlap);
}

/**
 * Extracts NGrams from a String of text.
 * Can handle ngrams of any length and also perform stop word removal before extraction
 * @param text the text that the ngrams should be extracted from
 * @param length the length of the ngrams
 * @param stopWords whether or not stopwords should be removed before extraction
 * @param overlap whether or not the ngrams should overlap
 */
public void extract(String text, int length, Boolean stopWords, Boolean overlap) throws FileNotFoundException, IOException {

    this.text = text;
    this.length = length;
    this.stopWords = stopWords;
    this.overlap = overlap;

    nGrams = new LinkedList<String>();
    uniqueNGrams = new LinkedList<String>();
    nGramFreqs = new HashMap<String, Integer>();

    /* If the minLength and maxLength are both 1, then we want unigrams
     * Make use of a StopAnalyzer when stopwords should be removed
     * Make use of a SimpleAnalyzer when stop words should be included
     */
    if (length == 1){
        if (this.stopWords) {
            analyzer = new StandardAnalyzer();
        }
        else {
            analyzer = new SimpleAnalyzer();
        }
    }
    else { //Bigger than unigrams so use ShingleAnalyzerWrapper. Once again, different analyzers depending on stop word removal
        if (this.stopWords) {
            analyzer = new ShingleAnalyzerWrapper(new StopAnalyzer(), length, length, " ", false, false, ""); //This is a hack to use Lucene 2.4 since in 2.4 position increments weren't preserved by default. Using a later version puts underscores (_) in the place of removed stop words.
        }
        else {
            analyzer = new ShingleAnalyzerWrapper(new SimpleAnalyzer(), length, length, " ", false, false, "");
        }
    }

    //Code to process and extract the ngrams
    TokenStream tokenStream = analyzer.tokenStream("text", new StringReader(this.text));
    //OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
    CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

    //int tokenCount = 0;
    while (tokenStream.incrementToken()) {
                    
        //int startOffset = offsetAttribute.startOffset();
        //int endOffset = offsetAttribute.endOffset();
        String termToken = charTermAttribute.toString(); //The actual token term
        nGrams.add(termToken); //Add all ngrams to the ngram LinkedList
        
        //If n-grams are not allowed to overlap, then increment to point of no overlap
        if (!overlap){
            for (int i = 0; i < length-1; i++){
                tokenStream.incrementToken();
            }
        }
        
    }

    //Store unique nGrams and frequencies in hash tables
    for (String nGram : nGrams) {
        if (nGramFreqs.containsKey(nGram)) {
            nGramFreqs.put(nGram, nGramFreqs.get(nGram)+1);
        }
        else {
            nGramFreqs.put(nGram, 1);
            uniqueNGrams.add(nGram);
        }
    }

}

/**
 * Returns the frequency of an ngram
 * @param ngram the ngram whose frequency should be returned
 * @return the frequency of the specified ngram
 */
public int getNGramFrequency(String ngram) {
    return nGramFreqs.get(ngram);
}

/**
 * Returns all ngrams
 * @return all ngrams
 */
public LinkedList<String> getNGrams() {
    return nGrams;
}

/**
 * Returns unique ngrams
 * @return the unique ngrams
 */
public LinkedList<String> getUniqueNGrams() {
    return uniqueNGrams;
}

/**
 * Sets whether or not stopword should be removed from the text
 * @param stopWords whether or not stopwords should be removed
 */
public void setStopWords(Boolean stopWords){
    this.stopWords = stopWords;
}

/**
 * Sets whether or not ngrams are allowed to overlap
 * @param overlap whether or not ngrams are allowed to overlap
 */
public void setOverlap(Boolean overlap){
    this.overlap = overlap;
}

}


