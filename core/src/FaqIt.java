import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.stream.XMLStreamException;

import com.faqit.similarity.Ranker;
import com.faqit.similarity.exception.RankerGeneralException;
import com.faqit.similarity.measures.exception.SimilarityMeasureException;
import com.faqit.storage.FAQImporter;
import com.faqit.storage.exception.RetrieveEntriesException;

/**
 * This is the client (entry point) using the FaqIt application.
 * 
 * @author FabioRibeiro
 * @author PauloMarques
 * 
 */
public class FaqIt {
	private static final boolean DEBUG = true;

	public static void main(String[] args) throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String query = null;
		String result = null;

		try {
			Ranker.init(DEBUG);
			if (args.length == 0) {
				while (true) {
					System.out.print("<Enter 'q' to quit>\nQuery: ");
					query = br.readLine();
					if (query.compareToIgnoreCase("q") == 0) {
						break;
					} else {
						result = Ranker.performQuery(query);
					}
					System.out.println("Answer: " + result + "\n");
				}
			} else {
				if (args.length == 3 && args[0].equalsIgnoreCase("l2rInput")) {
					FAQImporter.produceL2RInput(Ranker.getStorageManager(),
							args[1], Ranker.getNumFeatures(), args[2].equalsIgnoreCase("true")? true : false);
				} else if (args.length == 2
						&& args[0].equalsIgnoreCase("trainingInput")) {
					FAQImporter.produceTrainingInput(
							Ranker.getStorageManager(), args[1]);
				}
			}
		} catch (RankerGeneralException | XMLStreamException
				| RetrieveEntriesException | SimilarityMeasureException e) {
			System.out.println(e.getMessage());
		} finally {
			System.out.println("\nGoodbye!");
		}
	}
}
