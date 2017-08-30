import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class TMDBParsingProgram {

	public static void main(String[] args) {
		MovieInfoParsing parsing = new MovieInfoParsing();
		ArrayList<MovieInfo> list = new ArrayList<>();
		MovieInfo info;
		
		String json = "";
		String keyword = "스파이더맨";
		
		try {
			json = parsing.TMDBJsonData(keyword);
			 //System.out.println(json);

			JSONObject obj = new JSONObject(json);
			JSONArray results = (JSONArray) obj.get("results");
			for (int i = 0; i < results.length(); i++) {
				info = new MovieInfo();
				JSONObject entity = (JSONObject) results.get(i);
				
				info.setMovieNmEn(entity.getString("title"));
				info.setPosterImage(entity.getString("poster_path"));
				info.setBackdropImage(entity.getString("backdrop_path"));
				
				list.add(info);
			}

			for (MovieInfo m : list)
				System.out.println(m.toString());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
