import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class MovieInfoParsing2 {
	
	//TMDB에서 제공하는 OpenAPI JSON 파일을 String으로 변환하여 반환
	public MovieInfo TMDBJsonData(MovieInfo info) throws IOException {
		String json = "";
		BufferedReader br;
		
		String key = "fc26b37628734575187d1be55c6a3c85";
		String keyword = info.getMovieNmEn();
    	String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
        String apiURL;
        
        apiURL = "https://api.themoviedb.org/3/search/movie?"
        		+ "api_key=" + key + "&query=" + encodedKeyword + "&include_adult=false";	//해당연도의 영화를 알고싶으면 맨끝에 &year=2017 조건을 추가
        
        URL url = new URL(apiURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        
        br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
		
		json = br.readLine();
		
		JSONObject obj = new JSONObject(json);
		JSONArray results = (JSONArray) obj.get("results");
		//System.out.println(results.length()+" ");
		
		if(results.length()!=0) {
			for (int i = 0; i < results.length(); i++) {
				JSONObject entity = (JSONObject) results.get(0);
				String title = entity.getString("title").toLowerCase();
				String movieNmEn = info.getMovieNmEn().toLowerCase();
				
				if (title.equals(movieNmEn)) {
					/*String backdrop_path = entity.getString("backdrop_path");
					info.setBackdropImage(backdrop_path);*/
					info.setBackdropImage(entity.optString("backdrop_path"));
					
				}
			}
		}
		
		return info;
	}
	
	//Naver에서 제공하는 OpenAPI JSON 파일을 String으로 변환하여 반환
	public MovieInfo NaverJsonData(MovieInfo info) throws IOException {
		String json = "";
		BufferedReader br;
		
		String clientId = "aVyrhY81Hji8r3ApgQzx";
        String clientSecret = "e5vXcLz5J9";
        String keyword = info.getMovieNmEn();
        //System.out.println(keyword);
        String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
		
        String apiURL = "https://openapi.naver.com/v1/search/movie.json?query=" + encodedKeyword;
		
        URL url = new URL(apiURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-Naver-Client-Id", clientId); //발급받은ID
        connection.setRequestProperty("X-Naver-Client-Secret", clientSecret);//발급받은PW
        connection.setRequestProperty("Content-Type", "application/json");
        
        br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
        
        while((json=json+br.readLine()) != null) {
        	if(json.contains("null"))
        		break;
        }
        
        json = json.replace("null", "");
        json = json.replaceAll("<b>", "");
        json = json.replaceAll("</b>", "");
        
        JSONObject obj = new JSONObject(json);
        JSONArray items = (JSONArray) obj.get("items");
        for(int i=0; i<items.length(); i++) {
        	JSONObject entity = (JSONObject) items.get(i);
        	
        	String subtitle = entity.getString("subtitle").toLowerCase();
        	String movieNmEn = info.getMovieNmEn().toLowerCase();
        	
        	if(subtitle.equals(movieNmEn)) {
        		String link = entity.getString("link");
        		String replacedLink = link.replace("basic.nhn?code=", "photoViewPopup.nhn?movieCode=");
        		info.setPosterImage(replacedLink);
        	}
        }
        
		return info;
	}
	
	
	//영화진흥위원회 에서 제공하는 OpenAPI JSON 파일 파싱, 영화코드(movieCd) 가져오기
	public ArrayList<MovieInfo> KOBISJsonData(String keyword) throws IOException {
		ArrayList<MovieInfo> list = new ArrayList<>();
		MovieInfo info;
		
		String json = "";
		BufferedReader br;
		
		String key = "354c88719a60cd3da952a4be7dbf367e";
    	String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
        String apiURL;
        apiURL = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.json?key="
        		+key+"&movieNm="+encodedKeyword;

        URL url = new URL(apiURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        
        br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
		
		json = br.readLine();
		
		JSONObject obj = new JSONObject(json);
		JSONObject movieListResult = (JSONObject) obj.get("movieListResult");
		JSONArray movieList = (JSONArray) movieListResult.get("movieList");
        
		for(int i=0; i<movieList.length(); i++) {
			info = new MovieInfo();
			JSONObject entity = (JSONObject) movieList.get(i);
			
			String movieCd = entity.getString("movieCd");
			info.setMovieCd(movieCd);
			
			if(entity.getString("movieNmEn").equals("Package Screening")) continue;
			else KOBISJsonDataDetail(movieCd, info);
			
			list.add(info);
		}
		
		return list;
	}
	
	//가져온 영화코드(movieCd)로 영화 상세정보 가져오기
	public MovieInfo KOBISJsonDataDetail(String movieCd, MovieInfo info) throws IOException {
		String json = "";
		BufferedReader br;
		
		String key = "354c88719a60cd3da952a4be7dbf367e";
        String apiURL;
        apiURL = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json?key="
        		+key+"&movieCd="+movieCd;

        URL url = new URL(apiURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        
        br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
		
		json = br.readLine();
        
		JSONObject obj = new JSONObject(json);
		JSONObject movieInfoResult = (JSONObject) obj.get("movieInfoResult");
		JSONObject movieInfo = (JSONObject) movieInfoResult.get("movieInfo");
		
		info.setMovieNm(movieInfo.getString("movieNm"));
		info.setMovieNmEn(movieInfo.getString("movieNmEn"));
		info.setShowTm(movieInfo.getString("showTm"));
		info.setOpenDt(movieInfo.getString("openDt"));
		
		JSONArray nations = (JSONArray) movieInfo.get("nations");
		for(int i=0; i<nations.length(); i++) {
			JSONObject nation = (JSONObject) nations.get(i);
			info.setNations(nation.getString("nationNm")+"|");
		}
		
		JSONArray genres = (JSONArray) movieInfo.get("genres");
		for(int i=0; i<genres.length(); i++) {
			JSONObject genre = (JSONObject) genres.get(i);
			info.setGenres(genre.getString("genreNm")+"|");
		}
		
		JSONArray directors = (JSONArray) movieInfo.get("directors");
		for(int i=0; i<directors.length(); i++) {
			JSONObject director = (JSONObject) directors.get(i);
			info.setDirectors(director.getString("peopleNm")+"|");
		}
		
		JSONArray actors = (JSONArray) movieInfo.get("actors");
		for(int i=0; i<actors.length(); i++) {
			if(i==5) break;
			JSONObject actor = (JSONObject) actors.get(i);
			info.setActors(actor.getString("peopleNm")+"|");
		}
		
		JSONArray audits = (JSONArray) movieInfo.get("audits");
		for(int i=0; i<audits.length(); i++) {
			JSONObject audit = (JSONObject) audits.get(i);
			info.setWatchGradeNm(audit.getString("watchGradeNm"));
		}
		
		NaverJsonData(info);
		TMDBJsonData(info);
		
		return info;
	}
	
	
	
	
	
	
	//====================================================================================================================================
    //XmlPullParser를 이용하여 영화진응위원회 에서 제공하는 OpenAPI XML 파일 파싱하기(parsing)
    public ArrayList<MovieInfo> getXmlData(String keyword) throws IOException {
    	boolean inMovieNm = false, inOpenDt = false, inNationAlt = false, inGenreAlt = false;
        String movieNm = "", openDt = "", nationAlt = "", genreAlt = "";
    	
    	ArrayList<MovieInfo> list = new ArrayList<MovieInfo>();
    	MovieInfo info;
    	//http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.xml?key=354c88719a60cd3da952a4be7dbf367e&movieNm=추격자
    	String key = "354c88719a60cd3da952a4be7dbf367e";
    	String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
        String apiURL;
        apiURL = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.xml?key="
        		+ key + "&itemPerPage=1000&movieNm=" + encodedKeyword;

        URL url = new URL(apiURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/xml");

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(connection.getInputStream(), "UTF-8");  //xml 입력받기

            String tag;
            xpp.next();
            
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
//                System.out.println("\n while");
				switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:		//1. start태그를 읽으면서 해당태그면 변수 flag를 true로 바꿔줌
//                    	System.out.println("1: start tag");
                        tag = xpp.getName();    //태그 이름 얻어오기
//                        System.out.println("tag: "+tag);
                        
                        if (tag.equals("movieNm")) {
                        	inMovieNm = true;
                        }

                        if (tag.equals("prdtYear")) {
                        	inOpenDt = true;
                        }

                        if (tag.equals("nationAlt")) {
                        	inNationAlt = true;
                        }

                        if (tag.equals("genreAlt")) {
                        	inGenreAlt = true;
                        }

                        break;

                    case XmlPullParser.TEXT:		//2. 태그의 text를 임시변수에 넣어줌
//                    	System.out.println("2: text");
                        if (inMovieNm) {
//                        	System.out.println("2-1: text 읽어오기");
                        	movieNm = xpp.getText();
                            inMovieNm = false;
                        }
                        if (inOpenDt) {
//                        	System.out.println("2-2: text 읽어오기");
                        	openDt = xpp.getText();
                            inOpenDt = false;
                        }
                        if (inNationAlt) {
//                        	System.out.println("2-3: text 읽어오기");
                        	nationAlt = xpp.getText();
                            inNationAlt = false;
                        }
                        if (inGenreAlt) {
//                        	System.out.println("2-4: text 읽어오기");
                        	genreAlt = xpp.getText();
                            inGenreAlt = false;
                        }

                        break;

                    case XmlPullParser.END_TAG:		//3. 영화 1개가 끝나는 태그(movie)를 만나면 임시변수이 있는 값을 몽땅 클래스에 넣고 list배열에 추가함
//                    	System.out.println("3: end tag");
                        tag = xpp.getName();    //테그 이름 얻어오기
//                        System.out.println("tag: "+tag+"\n==============");
                        if (tag.equals("movie")) {
//                        	System.out.println("3-1: 리스트에 담기\n********************************************************");
                        	info = new MovieInfo();
                            info.setMovieNm(movieNm);
                            info.setOpenDt(openDt);
                            info.setNations(nationAlt);
                            info.setGenres(genreAlt);
                            list.add(info);
                        }
                        break;
                }

                eventType = xpp.next();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return list;
    }
}
