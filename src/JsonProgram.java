import java.io.IOException;
import java.util.ArrayList;

import org.json.*;

public class JsonProgram {

	public static void main(String[] args) {
		
		MovieInfoParsing parsing = new MovieInfoParsing();
		ArrayList<MovieInfo> list = new ArrayList<>();
		MovieInfo info;
		
		String json = "";
		try {
			json = parsing.getJsonData("아이언이글");
			
			JSONObject obj = new JSONObject(json);
			JSONObject movieListResult = (JSONObject) obj.get("movieListResult");
//			String source = movieListResult.getString("source");
//			System.out.println(source);
			
			
			JSONArray array = (JSONArray) movieListResult.get("movieList");
			
			for(int i=0; i<array.length(); i++) {
				info = new MovieInfo();
				JSONObject entity = (JSONObject) array.get(i);
				
				info.setMovieNm(entity.getString("movieNm"));
				info.setMovieNmEn(entity.getString("movieNmEn"));
				info.setOpenDt(entity.getString("openDt"));
				info.setNationAlt(entity.getString("nationAlt"));
				info.setGenreAlt(entity.getString("genreAlt"));
//				info.setDirectors(entity.getString("directors"));
				list.add(info);
			}
			
//			System.out.println(list.get(0).toString());
			
			for(MovieInfo m: list)
				System.out.println(m.toString());
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
