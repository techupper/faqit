import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.faqit.similarity.Ranker;
import com.faqit.similarity.exception.RankerGeneralException;

public class FaqIt {
	private static final boolean DEBUG = true;

	public static void main(String[] args) throws IOException{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String query = null;
		String result = null;
		
		try {
			Ranker.init(DEBUG);
			
			while(true){
				System.out.print("<Enter 'q' to quit>\nQuery: ");
				query = br.readLine();
				if(query.compareToIgnoreCase("q") == 0){
					break;
				}
				else{
					result = Ranker.performQuery(query);		
				}
				System.out.println("Result: " + result);
			}
		} catch (RankerGeneralException e1) {
			System.out.println(e1.getMessage());
		}finally{
			System.out.println("Goodbye!");			
		}
	}
}
