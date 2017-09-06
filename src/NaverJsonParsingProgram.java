import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class NaverJsonParsingProgram {

	public static void main(String[] args) {
		JsonParsing parsing = new JsonParsing();
		ArrayList<Movie> list = new ArrayList<>();
		Movie info;
		
		String json = "";
		String keyword = "�߰���";
		
		//Naver���� ������ ��ȭ ���� json �Ľ�
		try {
			json = parsing.naverMovieData(keyword);
			System.out.println(json);
			
			JSONObject obj = new JSONObject(json);
			
			JSONArray items = (JSONArray) obj.get("items");
			for(int i=0; i<items.length(); i++) {
				JSONObject entity = (JSONObject) items.get(i);
				
				info = new Movie();
				String imgUrl = entity.getString("link");
		    	String replacedImgUrl = imgUrl.replace("basic.nhn?code=", "photoViewPopup.nhn?movieCode=");
				info.setPosterImage(replacedImgUrl);
				info.setMovieNm(entity.getString("title"));		//Ű ���� movieNm(��ȭ����)�� ���� ���� info�� ����
				info.setMovieNmEn(entity.getString("subtitle"));
				info.setPrdtYear(entity.getString("pubDate"));
				info.setDirectors(entity.getString("director"));
				info.setActors(entity.getString("actor"));
				list.add(info);
			}
			
			for(Movie m: list)
				System.out.println(m.toString());
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
