import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import com.faqit.storage.*;

public class FaqIt {
	private static final boolean DEBUG = true;

	public static void main(String[] args) throws IOException,
			StoreEntryException, RetrieveEntriesException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String query = null;

		System.out.println("Starting the FaqIt engine...");
		
		Storage storageManager = new LuceneStorage(null);

		// 1. Start importing FAQs from xml to lucene 
		try {
			FAQImporter.importFAQ(storageManager);
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 2. Wait for user query and retrieve N most similar entries based on IF-IDF
		List<Entry> topEntries = null;
		
		while(true){
			System.out.print("<Enter 'q' to quit>\nQuery: ");
			query = br.readLine();
			if(query.compareToIgnoreCase("q") == 0){
				break;
			}
			else{
				topEntries = storageManager.RetrieveTopEntries(query);				
			}
		
			if(DEBUG){
				String storageResults = "Results retrieved by Storage: ";
				
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
		}

		// 3. Apply similarity measures to rank retrieved documents
		
		
		// 4. Apply Learning to rank algorithm to learn weights

		System.out.println("Stopping the FaqIt engine...");
	}

	@SuppressWarnings("unused")
	private static void testLuceneBasic() throws IOException,
			StoreEntryException, RetrieveEntriesException {
		
		String test_query = "what data";
		List<Entry> entries = new ArrayList<Entry>();

		entries.add(new Entry("1", "science", "what is data mining?",
				"I don't know"));
		entries.add(new Entry("2", "cs", "what the heck is lucene?",
				"lucene is the heck we are using in RGI's project"));
		entries.add(new Entry("3", "cs",
				"who is the father of computer science?", "alan turing"));
		entries.add(new Entry("4", "social", "are you alive?", "of course I am"));

		Storage storageManager = new LuceneStorage(null);

		for (Entry entry : entries) {
			storageManager.storeEntry(entry);
		}

		storageManager.RetrieveTopEntries(test_query);
	}
}
