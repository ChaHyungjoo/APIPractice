import java.io.IOException;
import java.net.URLEncoder;

public class KfmJsonParsingProgram {

	public static void main(String[] args) {
		
		JsonParsing parsing = new JsonParsing();
		String json = "";
		String keyword = "스파이더맨";
		
		
		
		try {
			
			json = parsing.kfmMovieData();
			System.out.println(json);
			
			//parsing.test();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
