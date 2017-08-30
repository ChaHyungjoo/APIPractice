import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class KOBISParsingProgram {

	public static void main(String[] args) {
		MovieInfoParsing parsing = new MovieInfoParsing();
		ArrayList<MovieInfo> list = new ArrayList<>();
		MovieInfo info;
		
		String json = "";
		String keyword = "���ڹ�";
		
		//��ȭ��������ȸ���� ������ ��ȭ ���� json �Ľ�
		try {
			json = parsing.KOBISJsonData(keyword);
			System.out.println(json);
			
			JSONObject obj = new JSONObject(json);			//�˻��� ��� ��ȭ�� ��� JSON���� �ڷḦ string���� ��ȯ�� json���� JSONObject���� ��ü obj�� ����
			JSONObject movieListResult = (JSONObject) obj.get("movieListResult");		//obj���� key���� movieListResult�� ������ �ٽ� JSONObject���� ��ü�� ����
//			String source = movieListResult.getString("source");
//			System.out.println(source);
			
			
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
				info.setOpenDt(entity.getString("openDt"));
				info.setNationAlt(entity.getString("nationAlt"));
				info.setGenreAlt(entity.getString("genreAlt"));
				
				JSONArray directors = (JSONArray) entity.get("directors");		//������ ���� ���� ���� �����Ƿ� JSONArray���� ��ü�� ����
				for(int j=0; j<directors.length(); j++) {
					JSONObject director = (JSONObject) directors.get(j);
					info.setDirectors(director.getString("peopleNm"));		//j�� ���鼭 Ű ���� peopleNm(������)�� ���� ���� info�� ����
				}
				
				list.add(info);		//��ȭ ����(info)�� ArrayList�� ��ü list�� �����
			}
			
			for(MovieInfo m: list)
				System.out.println(m.toString());
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
