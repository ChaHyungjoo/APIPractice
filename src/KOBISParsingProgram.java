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
		String keyword = "캐리비안의 해적: 망자의 함";
		String key = "354c88719a60cd3da952a4be7dbf367e";
		
		//영화진흥위원회에서 가져온 영화 정보 json 파싱
		try {
			json = parsing.KOBISJsonData(keyword);
			System.out.println(json);
			
			JSONObject obj = new JSONObject(json);			//검색된 모든 영화가 담긴 JSON형의 자료를 string으로 변환한 json으로 JSONObject형의 객체 obj를 만듦
			JSONObject movieListResult = (JSONObject) obj.get("movieListResult");		//obj에서 key값이 movieListResult인 것으로 다시 JSONObject형의 객체를 만듦
//			String source = movieListResult.getString("source");
//			System.out.println(source);
			
			
			JSONArray movieList = (JSONArray) movieListResult.get("movieList");		//검색된 영화가 여러 개일 수 있으므로 키 값이 movieList인 것을 JSONArray형의 객체  movieList에 담아줌
			
			for(int i=0; i<movieList.length(); i++) {
				info = new MovieInfo();		//영화 정보를 담을 객체 info를 생성
				JSONObject entity = (JSONObject) movieList.get(i);		//movieList중 i번째 영화를 JSONObject형 객체 entity에 담아줌
				
				info.setMovieCd(entity.getString("movieCd"));
				info.setMovieNm(entity.getString("movieNm"));		//키 값이 movieNm(영화제목)인 것의 값을 info에 셋팅
				if(entity.getString("movieNmEn").equals("Package Screening"))
					continue;
				else
					info.setMovieNmEn(entity.getString("movieNmEn"));
				info.setPrdtYear(entity.getString("prdtYear"));
				info.setOpenDt(entity.getString("openDt"));
				info.setNations(entity.getString("nationAlt"));
				info.setGenres(entity.getString("genreAlt"));
				
				JSONArray directors = (JSONArray) entity.get("directors");		//감독이 여러 명일 수도 있으므로 JSONArray형의 객체로 만듦
				for(int j=0; j<directors.length(); j++) {
					JSONObject director = (JSONObject) directors.get(j);
					info.setDirectors(director.getString("peopleNm"));		//j번 돌면서 키 값이 peopleNm(감독명)인 것의 값을 info에 셋팅
				}
				
				list.add(info);		//영화 정보(info)를 ArrayList형 객체 list에 담아줌
			}
			
			for(MovieInfo m: list)
				System.out.println(m.toString());
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
