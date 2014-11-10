package com.faqit.similarity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import com.faqit.storage.Entry;
import com.faqit.storage.FAQImporter;
import com.faqit.storage.LuceneStorage;
import com.faqit.storage.RetrieveEntriesException;
import com.faqit.storage.Storage;
import com.faqit.storage.StoreEntryException;

public class Ranker {
	
	private static Ranker INSTANCE = new Ranker();
	
	private static Storage storageManager;
	private static boolean initialized = false;
	private static boolean debug;
	
	//private List<SimilarityMeasure> measures;
	
	private Ranker(){
	}
	
	public static Ranker getInstance(){
		return INSTANCE;
	}
	
	public static void Init(boolean onDebug) throws RankerGeneralException{
		try {
			storageManager = new LuceneStorage(null);		
			debug = onDebug;
			
			// 1. Start importing FAQs from xml to lucene 
			FAQImporter.importFAQ(storageManager);
			
			initialized = true;
		} catch (FileNotFoundException e) {
			throw new RankerGeneralException("I have lost something, not sure what, but...");
		} catch (IOException e) {
			throw new RankerGeneralException("I feel like there is a wall between us...");
		} catch (StoreEntryException e) {
			throw new RankerGeneralException("I fear I will not remember what you have just told me about...");
		} catch (XMLStreamException e) {
			throw new RankerGeneralException("I cannot see clearly!");
		}
	}

	public static String performQuery(String query) throws RankerGeneralException{
		if(!initialized){
			throw new RankerGeneralException("Ranker was not initialized. Call Ranker.Init()");
		}
		String result = null;
		try {
			// 2. Wait for user query and retrieve N most similar entries based on IF-IDF
			List<Entry> topEntries = storageManager.RetrieveTopEntries(query);
			
			if(debug){
				String storageResults = "Results retrieved by Storage: \n";
				
				if(topEntries.isEmpty()){
					storageResults += "<No similar entries for " + query +">";
				}
				else{
					int i = 1;
					for(Entry e : topEntries){
						storageResults += "\t" + i + ": " + e.getQuestion() + "\n";
						i++;
					}					
				}
				System.out.println(storageResults + "\n");
			}
			
			// 3. Apply similarity measures to rank retrieved documents
			
			// 4. Apply Learning to rank algorithm to learn weights
			
			// 5. Return the top 1 similar question
			if(topEntries.size() > 0){
				result = topEntries.get(0).getQuestion();				
			}
			
		} catch (RetrieveEntriesException e1) {
			throw new RankerGeneralException("At the moment it is not possible to process your query.");
		}
		
		return result;
	}

}
