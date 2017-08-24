import java.io.IOException;
import java.util.ArrayList;

import org.json.*;

public class JsonProgram {

	public static void main(String[] args) {
		
		MovieInfoParsing parsing = new MovieInfoParsing();
//		ArrayList<MovieInfo> movieInfo = new ArrayList<>();
		ArrayList<MovieInfo> list = new ArrayList<>();
		MovieInfo info = new MovieInfo();
		
		String json = "";
		try {
			json = parsing.getJsonData("°î¼º");
			
			JSONObject obj = new JSONObject(json);
			JSONObject movieListResult = (JSONObject) obj.get("movieListResult");
			
//			String source = movieListResult.getString("source");
//			System.out.println(source);
			
			
			JSONArray array = (JSONArray) movieListResult.get("movieList");
			
			for(int i=0; i<array.length(); i++) {
				JSONObject entity = (JSONObject) array.get(i);
				
				info.setMovieNm(entity.getString("movieNm"));
				info.setMovieNmEn(entity.getString("movieNmEn"));
				info.setOpenDt(entity.getString("openDt"));
				info.setNationAlt(entity.getString("nationAlt"));
				info.setGenreAlt(entity.getString("genreAlt"));
//				info.setDirectors(entity.getString("directors"));
				list.add(info);
			}
			
			System.out.println(list.get(0).toString());
			
			/*for(MovieInfo m: list)
				System.out.println(m.toString());*/
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

/*		String str = "{\"firstname\":\"Jesper\",\"surname\":\"Aaberg\",\"phone\":[\"555-0100\",\"555-0120\"]}";
		
		JSONObject obj = new JSONObject(str);
		
		
		String firstname = null;
		String surname = null;
		ArrayList<String> phone = new ArrayList<>();
		
		firstname = obj.getString("firstname");
		surname = obj.getString("surname");
		
		JSONArray array = (JSONArray)obj.get("phone");
		System.out.println(array.length());
		
		for(int i=0; i<array.length(); i++) {
//			String _phone = (String)array.get(i);
			String _phone = array.getString(i);
			phone.add(_phone);
		}
		
		System.out.println(firstname);
		System.out.println(surname);
		System.out.println(phone.get(0));
		System.out.println(phone.get(1));*/
		
		
		
	}

}
