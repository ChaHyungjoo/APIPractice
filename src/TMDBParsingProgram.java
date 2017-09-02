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
		String keyword = "캐리비안의 해적";
		String key = "fc26b37628734575187d1be55c6a3c85";
		
		try {
			json = parsing.TMDBJsonData(keyword);
			 System.out.println(json);

			JSONObject obj = new JSONObject(json);
			JSONArray results = (JSONArray) obj.get("results");
			//System.out.println(results.length());
			if(results.length()!=0) {
				
				for (int i = 0; i < results.length(); i++) {
					info = new MovieInfo();
					JSONObject entity = (JSONObject) results.get(i);
					
					info.setMovieId(entity.getInt("id"));
					info.setMovieNm(entity.getString("title"));
					info.setMovieNmEn(entity.getString("original_title"));
					info.setPrdtYear(entity.getString("release_date"));
					info.setPosterImage(entity.optString("poster_path"));
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
