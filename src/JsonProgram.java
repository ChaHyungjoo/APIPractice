import java.io.IOException;
import java.util.ArrayList;

import org.json.*;

public class JsonProgram {

	public static void main(String[] args) {
		
		MovieInfoParsing parsing = new MovieInfoParsing();
		ArrayList<MovieInfo> list = new ArrayList<>();
		MovieInfo info;
		
		String json = "";
		String keyword = "�����̴���";
		
		//Naver���� ������ ��ȭ ���� json �Ľ�
		try {
			String str1 = parsing.NaverJsonData(keyword);
			String str2 = str1.replaceAll("<b>","");
			json = str2.replaceAll("</b>", "");
//			System.out.println(json);
			
			JSONObject obj = new JSONObject(json);
			
			JSONArray items = (JSONArray) obj.get("items");
			for(int i=0; i<items.length(); i++) {
				info = new MovieInfo();
				JSONObject entity = (JSONObject) items.get(i);
				
				info.setMovieNm(entity.getString("title"));
				info.setMovieNmEn(entity.getString("subtitle"));
				
				String imgUrl = entity.getString("link");
		    	String replacedImgUrl = imgUrl.replace("basic.nhn?code=", "photoViewPopup.nhn?movieCode=");
		    	
		    	info.setPosterImage(replacedImgUrl);
				
				list.add(info);
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//��ȭ��������ȸ���� ������ ��ȭ ���� json �Ľ�
		try {
			json = parsing.KOBISJsonData(keyword);
			//System.out.println(json);
			
			JSONObject obj = new JSONObject(json);			//�˻��� ��� ��ȭ�� ��� JSON���� �ڷḦ string���� ��ȯ�� json���� JSONObject���� ��ü obj�� ����
			JSONObject movieListResult = (JSONObject) obj.get("movieListResult");		//obj���� key���� movieListResult�� ������ �ٽ� JSONObject���� ��ü�� ����
			
			JSONArray movieList = (JSONArray) movieListResult.get("movieList");		//�˻��� ��ȭ�� ���� ���� �� �����Ƿ� Ű ���� movieList�� ���� JSONArray���� ��ü  movieList�� �����
			
			for(int i=0; i<movieList.length(); i++) {
				JSONObject entity = (JSONObject) movieList.get(i);		//movieList�� i��° ��ȭ�� JSONObject�� ��ü entity�� �����
				String movieNm = entity.getString("movieNm");
				String movieNmEn = entity.getString("movieNmEn");
				
				if(movieNmEn.equals(list.get(i).getMovieNmEn())) {
					list.get(i).setOpenDt(entity.getString("openDt"));
					list.get(i).setNationAlt(entity.getString("nationAlt"));
					list.get(i).setGenreAlt(entity.getString("genreAlt"));
					
					JSONArray directors = (JSONArray) entity.get("directors");		//������ ���� ���� ���� �����Ƿ� JSONArray���� ��ü�� ����
					for(int j=0; j<directors.length(); j++) {
						JSONObject director = (JSONObject) directors.get(j);
						list.get(j).setDirectors(director.getString("peopleNm"));		//j�� ���鼭 Ű ���� peopleNm(������)�� ���� ���� info�� ����
					}
				}
				
			}
			
			for(MovieInfo m: list)
				System.out.println(m.toString());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	/*	try {
			json = parsing.TMDBJsonData(keyword);
			// System.out.println(json);

			JSONObject obj = new JSONObject(json);
			JSONArray results = (JSONArray) obj.get("results");
			for (int i = 0; i < results.length(); i++) {
				JSONObject entity = (JSONObject) results.get(i);
				
				String subtitle = entity.getString("title");
				for(int j=0; j<list.size(); j++) {
					
					if(subtitle.equals(list.get(j).getMovieNmEn())) {
						String imgUrl = entity.getString("backdrop_path");
						list.get(j).setBackdropImage(imgUrl);
					}
				}
				
				
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
		}*/
	

	}

}
