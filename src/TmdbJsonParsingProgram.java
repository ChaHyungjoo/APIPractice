import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class TmdbJsonParsingProgram {

	public static void main(String[] args) {
		JsonParsing parsing = new JsonParsing();
		ArrayList<Movie> list = new ArrayList<>();
		
		String keyword = "ºÐÈ«½Å";
		String key = "fc26b37628734575187d1be55c6a3c85";
		
		try {
			list = parsing.tmdbMovieData(keyword);
			
			for (Movie m : list)
				System.out.println(m.toString());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
