import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class NaverParsingProgram {

	public static void main(String[] args) {
		MovieInfoParsing parsing = new MovieInfoParsing();
		ArrayList<MovieInfo> list = new ArrayList<>();
		MovieInfo info;
		
		String json = "";
		String keyword = "발레리안";
		
		//Naver에서 가져온 영화 정보 json 파싱
		try {
			json = parsing.NaverJsonData(keyword);
			System.out.println(json);
			
			JSONObject obj = new JSONObject(json);
			
			JSONArray items = (JSONArray) obj.get("items");
			for(int i=0; i<items.length(); i++) {
				JSONObject entity = (JSONObject) items.get(i);
				
				info = new MovieInfo();
				String imgUrl = entity.getString("link");
		    	String replacedImgUrl = imgUrl.replace("basic.nhn?code=", "photoViewPopup.nhn?movieCode=");
				info.setPosterImage(replacedImgUrl);
				info.setMovieNm(entity.getString("title"));		//키 값이 movieNm(영화제목)인 것의 값을 info에 셋팅
				info.setMovieNmEn(entity.getString("subtitle"));
				info.setOpenDt(entity.getString("pubDate"));
				info.setDirectors(entity.getString("director"));
				info.setActors(entity.getString("actor"));
				list.add(info);
			}
			
			for(MovieInfo m: list)
				System.out.println(m.toString());
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
