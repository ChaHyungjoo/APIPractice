import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class TMDBParsingProgram {

	public static void main(String[] args) {
		MovieInfoParsing parsing = new MovieInfoParsing();
		ArrayList<MovieInfo> list = new ArrayList<>();
		MovieInfo info;
		
		String json = "";
		String keyword = "titanic";
		
		try {
			json = parsing.TMDBJsonData(keyword);
			 //System.out.println(json);

			JSONObject obj = new JSONObject(json);
			JSONArray results = (JSONArray) obj.get("results");
			//System.out.println(results.length());
			if(results.length()!=0) {
				
				for (int i = 0; i < results.length(); i++) {
					info = new MovieInfo();
					JSONObject entity = (JSONObject) results.get(i);
					
					info.setMovieNmEn(entity.getString("title"));
					String posterImage = "https://image.tmdb.org/t/p/w1920" + entity.optString("poster_path");
					info.setPosterImage(posterImage);
					info.setBackdropImage(entity.optString("backdrop_path"));
					
					list.add(info);
				}
				
			}else System.out.println("결과 없음");

			for (MovieInfo m : list)
				System.out.println(m.toString());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
