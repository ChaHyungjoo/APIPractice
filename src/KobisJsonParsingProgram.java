import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class KobisJsonParsingProgram {

	public static void main(String[] args) {
		JsonParsing parsing = new JsonParsing();
		ArrayList<Movie> list = new ArrayList<>();
		
		String keyword = "스타 트랙";
		String key = "354c88719a60cd3da952a4be7dbf367e";
		
		try {
			list = parsing.movieData(keyword);
			
			for(Movie m : list)
				System.out.println(m.toString());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
