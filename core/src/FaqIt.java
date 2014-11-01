import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.faqit.storage.*;

public class FaqIt {

	public static void main(String[] args) throws IOException,
			StoreEntryException, RetrieveEntriesException {
		// Entry point of the application
		System.out.println("Starting the FaqIt engine...");

		// 1. Start importing FAQs from xml

		// 2. Store each entry in storage (lucene)

		// 3. Wait for user query

		// 4. If there is user input, retrieve from storage the top N best
		// documents

		// 5. Apply similarity measures to rank the previously retrieved
		// documents

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

		StorageInterface storageManager = new LuceneStorage(null);

		for (Entry entry : entries) {
			storageManager.StoreEntry(entry);
		}

		storageManager.RetrieveTopEntries(test_query);
	}
}
