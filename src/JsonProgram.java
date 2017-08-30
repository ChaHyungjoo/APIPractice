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
		
		//��ȭ��������ȸ���� ������ ��ȭ ���� json �Ľ�
		try {
			json = parsing.KOBISJsonData(keyword);
			//System.out.println(json);
			
			JSONObject obj = new JSONObject(json);			//�˻��� ��� ��ȭ�� ��� JSON���� �ڷḦ string���� ��ȯ�� json���� JSONObject���� ��ü obj�� ����
			JSONObject movieListResult = (JSONObject) obj.get("movieListResult");		//obj���� key���� movieListResult�� ������ �ٽ� JSONObject���� ��ü�� ����
			
			JSONArray movieList = (JSONArray) movieListResult.get("movieList");		//�˻��� ��ȭ�� ���� ���� �� �����Ƿ� Ű ���� movieList�� ���� JSONArray���� ��ü  movieList�� �����
			for(int i=0; i<movieList.length(); i++) {
				info = new MovieInfo();		//��ȭ ������ ���� ��ü info�� ����
				JSONObject entity = (JSONObject) movieList.get(i);		//movieList�� i��° ��ȭ�� JSONObject�� ��ü entity�� �����
				
				info.setMovieCd(entity.getString("movieCd"));
				info.setMovieNm(entity.getString("movieNm"));		//Ű ���� movieNm(��ȭ����)�� ���� ���� info�� ����
				if(entity.getString("movieNmEn").equals("Package Screening"))
					continue;
				else
					info.setMovieNmEn(entity.getString("movieNmEn"));
				
				String showTm = parsing.KOBISJsonDataDetail(entity.getString("movieCd"));
				info.setShowTm(showTm);
				info.setOpenDt(entity.getString("openDt"));
				info.setNations(entity.getString("nationAlt"));
				info.setGenres(entity.getString("genreAlt"));
				
				JSONArray directors = (JSONArray) entity.get("directors");		//������ ���� ���� ���� �����Ƿ� JSONArray���� ��ü�� ����
				for(int j=0; j<directors.length(); j++) {
					JSONObject director = (JSONObject) directors.get(j);
					info.setDirectors(director.getString("peopleNm")+"|");		//j�� ���鼭 Ű ���� peopleNm(������)�� ���� ���� info�� ����
				}
				
				list.add(info);		//��ȭ ����(info)�� ArrayList�� ��ü list�� �����
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Naver���� ������ ��ȭ ���� json �Ľ�
		try {
			json = parsing.NaverJsonData(keyword);
			//System.out.println(json);
			
			JSONObject obj = new JSONObject(json);
			
			JSONArray items = (JSONArray) obj.get("items");
			for(int i=0; i<items.length(); i++) {
				JSONObject entity = (JSONObject) items.get(i);
				
				for(int j=0; j<list.size(); j++) {
					String subtitle = entity.getString("subtitle");
					if(subtitle.equals(list.get(j).getMovieNmEn())) {
						String imgUrl = entity.getString("link");
				    	String replacedImgUrl = imgUrl.replace("basic.nhn?code=", "photoViewPopup.nhn?movieCode=");
				    	list.get(j).setPosterImage(replacedImgUrl);
				    	list.get(j).setActors(entity.getString("actor"));
					}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			json = parsing.TMDBJsonData(keyword);
			// System.out.println(json);

			JSONObject obj = new JSONObject(json);
			JSONArray results = (JSONArray) obj.get("results");
			for (int i = 0; i < results.length(); i++) {
				JSONObject entity = (JSONObject) results.get(i);
				
				String subtitle = entity.getString("title");
				for(int j=0; j<list.size(); j++) {
					
					if(subtitle.equals(list.get(j).getMovieNmEn())){
						String imgUrl = entity.getString("backdrop_path");
						list.get(j).setBackdropImage(imgUrl);
					}
				}
			}

			for (MovieInfo m : list)
				System.out.println(m.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}
	

	}

}
