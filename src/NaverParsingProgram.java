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
		String keyword = "�߷�����";
		
		//Naver���� ������ ��ȭ ���� json �Ľ�
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
				info.setMovieNm(entity.getString("title"));		//Ű ���� movieNm(��ȭ����)�� ���� ���� info�� ����
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
