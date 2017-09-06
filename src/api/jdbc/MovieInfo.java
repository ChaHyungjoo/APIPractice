package api.jdbc;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class MovieInfo {
	
	//�쁺�솕吏꾪씎�쐞�썝�쉶 �뿉�꽌 �젣怨듯븯�뒗 OpenAPI JSON �뙆�씪 �뙆�떛, �쁺�솕肄붾뱶(movieCd) + �긽�꽭�젙蹂� + �씠誘몄� 遺숈뿬�꽌 由ъ뒪�듃 援ы쁽�빐�꽌 諛섑솚
	public ArrayList<Movie> movieData(String keyword) throws IOException {
		ArrayList<Movie> list = new ArrayList<>();
		Movie movie;
		
		String json = "";
		BufferedReader br;
		
		String key = "febb1987881018460c2e26f49f1a23b1";
					//형주: febb1987881018460c2e26f49f1a23b1 354c88719a60cd3da952a4be7dbf367e
    	String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
        String apiURL;
        apiURL = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.json?key="
        		+ key + "&movieNm=" + encodedKeyword + "&itemPerPage=100";

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
			movie = new Movie();
			JSONObject entity = (JSONObject) movieList.get(i);
			
			String movieCd = entity.getString("movieCd");
			movie.setMovieCd(movieCd);
			
			if(entity.getString("movieNmEn").equals("Package Screening")) continue;
			//else if(entity.getString("prdtYear").equals("")) continue;
			//else if(entity.getString("openDt").equals("")) continue;
			else if(entity.getString("movieNm").contains("+")) continue;
			else 
				movie = movieDataDetail(movie, movieCd);
			
			list.add(movie);
		}
		
		br.close();
		connection.disconnect();
		
		return list;
	}
	
	
	//媛��졇�삩 �쁺�솕肄붾뱶(movieCd)濡� �쁺�솕 �긽�꽭�젙蹂� + �꽕�씠踰� �쁺�솕 �룷�뒪�꽣 �씠誘몄� + TMDB �쁺�솕 �뒪�떥而� �씠誘몄� 遺숈씠湲�
	public Movie movieDataDetail(Movie movie, String movieCd) throws IOException {
		String json = "";
		BufferedReader br;
		
		String key = "febb1987881018460c2e26f49f1a23b1";
					//형주: febb1987881018460c2e26f49f1a23b1 354c88719a60cd3da952a4be7dbf367e
        String apiURL;
        apiURL = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json?key="
        		+ key + "&movieCd=" + movieCd;

        URL url = new URL(apiURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        
        br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
		
		json = br.readLine();
        
		JSONObject obj = new JSONObject(json);
		JSONObject movieInfoResult = (JSONObject) obj.get("movieInfoResult");
		JSONObject movieInfo = (JSONObject) movieInfoResult.get("movieInfo");
		
		movie.setMovieNm(movieInfo.getString("movieNm"));
		movie.setMovieNmEn(movieInfo.getString("movieNmEn"));
		movie.setShowTm(movieInfo.getString("showTm"));
		movie.setPrdtYear(movieInfo.getString("prdtYear"));
		movie.setOpenDt(movieInfo.getString("openDt"));
		
		JSONArray nations = (JSONArray) movieInfo.get("nations");
		for(int i=0; i<nations.length(); i++) {
			JSONObject nation = (JSONObject) nations.get(i);
			movie.setNations(nation.getString("nationNm")+"/");
		}
		
		JSONArray genres = (JSONArray) movieInfo.get("genres");
		for(int i=0; i<genres.length(); i++) {
			JSONObject genre = (JSONObject) genres.get(i);
			movie.setGenres(genre.getString("genreNm")+"/");
		}
		
		JSONArray directors = (JSONArray) movieInfo.get("directors");
		for(int i=0; i<directors.length(); i++) {
			JSONObject director = (JSONObject) directors.get(i);
			movie.setDirectors(director.getString("peopleNm")+"/");
		}
		
		JSONArray actors = (JSONArray) movieInfo.get("actors");
		for(int i=0; i<actors.length(); i++) {
			if(i==5) break;
			JSONObject actor = (JSONObject) actors.get(i);
			movie.setActors(actor.getString("peopleNm")+"/");
		}
		
		JSONArray audits = (JSONArray) movieInfo.get("audits");
		for(int i=0; i<audits.length(); i++) {
			JSONObject audit = (JSONObject) audits.get(i);
			movie.setWatchGradeNm(audit.getString("watchGradeNm"));
		}
		
		String prdtYear = movieInfo.getString("prdtYear");
		String movieNmEn = movie.getMovieNmEn();
		String movieNm = movie.getMovieNm();
		
		//naverMovieData(movie, prdtYear);
		movie = tmdbMovieData(movie, prdtYear, movieNmEn);
		
		if(movie.getPosterImage()==null || movie.getBackdropImage()==null || movie.getOverView()==null)
			movie = tmdbMovieData(movie, prdtYear, movieNm);
		
		br.close();
		connection.disconnect();
		
		return movie;
	}

	
	//TMDB�뿉�꽌 �젣怨듯븯�뒗 OpenAPI JSON �뙆�씪�쓣 String�쑝濡� 蹂��솚�븯�뿬 諛섑솚
	public Movie tmdbMovieData(Movie movie, String prdtYear, String movieNmEn) throws IOException {
		String json = "";
		BufferedReader br;
		
		String key = "fc26b37628734575187d1be55c6a3c85";
					//형주: fc26b37628734575187d1be55c6a3c85
		String keyword = movieNmEn.toLowerCase();
		if(keyword.length()==0)
        	keyword = movie.getMovieNm();
    	String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
        String apiURL;
        
        apiURL = "https://api.themoviedb.org/3/search/movie?"
        		+ "api_key=" + key + "&language=ko&query=" + encodedKeyword 
        		+ "&include_adult=false";	//�빐�떦�뿰�룄�쓽 �쁺�솕瑜� �븣怨좎떢�쑝硫� 留⑤걹�뿉 &year=2017 議곌굔�쓣 異붽�
        
        URL url = new URL(apiURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        
        br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
		
		json = br.readLine();
		
		JSONObject obj = new JSONObject(json);
		JSONArray results = (JSONArray) obj.get("results");
		
		if(results.length()>1) {
			
			for (int i = 0; i < results.length(); i++) {
				JSONObject entity = (JSONObject) results.get(i);
				
				String release_date = entity.optString("release_date", null);
				if(!release_date.equals(""))
					release_date = release_date.substring(0, 4);
				String openDt = movie.getOpenDt();
				if(!openDt.equals(""))
					openDt = movie.getOpenDt().substring(0,4);
				
				String title = entity.getString("title").toLowerCase();
				String original_title = entity.getString("original_title").toLowerCase();
				
				if (release_date.equals(prdtYear) && keyword.contains(original_title)) {
					movie.setMovieId(entity.getInt("id"));
					movie.setPosterImage(entity.optString("poster_path"));
					movie.setBackdropImage(entity.optString("backdrop_path"));
					movie.setOverView(entity.getString("overview"));
				}
				else if (release_date.equals(prdtYear) && keyword.contains(title)) {
					movie.setMovieId(entity.getInt("id"));
					movie.setPosterImage(entity.optString("poster_path"));
					movie.setBackdropImage(entity.optString("backdrop_path"));
					movie.setOverView(entity.getString("overview"));
				}
				else if (release_date.equals(openDt) && keyword.contains(original_title) && movie.getPosterImage()==null) {
					movie.setMovieId(entity.getInt("id"));
					movie.setPosterImage(entity.optString("poster_path"));
					movie.setBackdropImage(entity.optString("backdrop_path"));
					movie.setOverView(entity.getString("overview"));
				}
				else if (release_date.equals(openDt) && keyword.contains(title) && movie.getPosterImage()==null) {
					movie.setMovieId(entity.getInt("id"));
					movie.setPosterImage(entity.optString("poster_path"));
					movie.setBackdropImage(entity.optString("backdrop_path"));
					movie.setOverView(entity.getString("overview"));
				}
			}
		}
		else if(results.length()==1) {
			JSONObject entity = (JSONObject) results.get(0);
			
			/*movie.setMovieId(entity.getInt("id"));
			movie.setPosterImage(entity.optString("poster_path"));
			movie.setBackdropImage(entity.optString("backdrop_path"));
			movie.setOverView(entity.getString("overview"));*/
			
			String release_date = entity.optString("release_date", null);
			if(!release_date.equals(""))
				release_date = release_date.substring(0, 4);
			String openDt = movie.getOpenDt();
			if(!openDt.equals(""))
				openDt = movie.getOpenDt().substring(0,4);
			
			String title = entity.getString("title").toLowerCase();
			String original_title = entity.getString("original_title").toLowerCase();
			
			if (release_date.equals(prdtYear)) {
				movie.setMovieId(entity.getInt("id"));
				movie.setPosterImage(entity.optString("poster_path"));
				movie.setBackdropImage(entity.optString("backdrop_path"));
				movie.setOverView(entity.getString("overview"));
			}
			/*else if (release_date.equals(prdtYear) && keyword.contains(title)) {
				movie.setMovieId(entity.getInt("id"));
				movie.setPosterImage(entity.optString("poster_path"));
				movie.setBackdropImage(entity.optString("backdrop_path"));
				movie.setOverView(entity.getString("overview"));
			}
			else if (release_date.equals(openDt) && keyword.contains(original_title) && movie.getPosterImage()==null) {
				movie.setMovieId(entity.getInt("id"));
				movie.setPosterImage(entity.optString("poster_path"));
				movie.setBackdropImage(entity.optString("backdrop_path"));
				movie.setOverView(entity.getString("overview"));
			}
			else if (release_date.equals(openDt) && keyword.contains(title) && movie.getPosterImage()==null) {
				movie.setMovieId(entity.getInt("id"));
				movie.setPosterImage(entity.optString("poster_path"));
				movie.setBackdropImage(entity.optString("backdrop_path"));
				movie.setOverView(entity.getString("overview"));
			}*/
		}
		
		int movieId = movie.getMovieId();
		
		if(movieId!=0)
			movie = tmdbMovieTrailer(movie, movieId);
		
		br.close();
		connection.disconnect();
		
		return movie;
	}
	
	public Movie tmdbMovieTrailer(Movie movie, int movieId) throws IOException {
		String json = "";
		BufferedReader br;
		
		String key = "fc26b37628734575187d1be55c6a3c85";
		String apiURL;
		apiURL = "http://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=" + key;
		//http://api.themoviedb.org/3/movie/437068/videos?api_key=fc26b37628734575187d1be55c6a3c85
		
		URL url = new URL(apiURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        
        br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
		
		json = br.readLine();
		JSONObject obj = new JSONObject(json);
		JSONArray results = (JSONArray) obj.get("results");
		
		if(results.length()!=0) {
			
			for (int i = 0; i < results.length(); i++) {
				JSONObject entity = (JSONObject) results.get(0);
				movie.setTrailer(entity.getString("key"));
				
			}
			
		}
		
		br.close();
		connection.disconnect();
		
		return movie;
	}
	
	
	
	/*	//Naver�뿉�꽌 �젣怨듯븯�뒗 OpenAPI JSON �뙆�씪�쓣 String�쑝濡� 蹂��솚�븯�뿬 諛섑솚
	public MovieInfo naverMovieData(MovieInfo movie, String prdtYear) throws IOException {
		String json = "";
		BufferedReader br;
		
		String clientId = "aVyrhY81Hji8r3ApgQzx";
        String clientSecret = "e5vXcLz5J9";
        String keyword = movie.getMovieNm();
        if(keyword.length()==0)
        	keyword = movie.getMovieNmEn();
        String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
		
        String apiURL = "https://openapi.naver.com/v1/search/movie.json?query=" + encodedKeyword + "&display=100";
		
        URL url = new URL(apiURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-Naver-Client-Id", clientId); //諛쒓툒諛쏆�ID
        connection.setRequestProperty("X-Naver-Client-Secret", clientSecret);//諛쒓툒諛쏆�PW
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
        	String pubDate = entity.getString("pubDate");
        	String title = entity.getString("title");
        	
        	if(title.equals(keyword) && prdtYear.equals(pubDate)) {
        		String link = entity.getString("link");
        		String replacedLink = link.replace("basic.nhn?code=", "photoViewPopup.nhn?movieCode=");
        		movie.setPosterImage(replacedLink);
        	}
        }
        
        br.close();
		connection.disconnect();
        
		return movie;
	}*/
	
	//====================================================================================================================================
/*    //XmlPullParser瑜� �씠�슜�븯�뿬 �쁺�솕吏꾩쓳�쐞�썝�쉶 �뿉�꽌 �젣怨듯븯�뒗 OpenAPI XML �뙆�씪 �뙆�떛�븯湲�(parsing)
    public ArrayList<MovieInfo> getXmlData(String keyword) throws IOException {
    	boolean inMovieNm = false, inOpenDt = false, inNationAlt = false, inGenreAlt = false;
        String movieNm = "", openDt = "", nationAlt = "", genreAlt = "";
    	
    	ArrayList<MovieInfo> list = new ArrayList<MovieInfo>();
    	MovieInfo info;
    	//http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.xml?key=354c88719a60cd3da952a4be7dbf367e&movieNm=異붽꺽�옄
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
            xpp.setInput(connection.getInputStream(), "UTF-8");  //xml �엯�젰諛쏄린

            String tag;
            xpp.next();
            
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
//                System.out.println("\n while");
				switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:		//1. start�깭洹몃�� �씫�쑝硫댁꽌 �빐�떦�깭洹몃㈃ 蹂��닔 flag瑜� true濡� 諛붽퓭以�
//                    	System.out.println("1: start tag");
                        tag = xpp.getName();    //�깭洹� �씠由� �뼸�뼱�삤湲�
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

                    case XmlPullParser.TEXT:		//2. �깭洹몄쓽 text瑜� �엫�떆蹂��닔�뿉 �꽔�뼱以�
//                    	System.out.println("2: text");
                        if (inMovieNm) {
//                        	System.out.println("2-1: text �씫�뼱�삤湲�");
                        	movieNm = xpp.getText();
                            inMovieNm = false;
                        }
                        if (inOpenDt) {
//                        	System.out.println("2-2: text �씫�뼱�삤湲�");
                        	openDt = xpp.getText();
                            inOpenDt = false;
                        }
                        if (inNationAlt) {
//                        	System.out.println("2-3: text �씫�뼱�삤湲�");
                        	nationAlt = xpp.getText();
                            inNationAlt = false;
                        }
                        if (inGenreAlt) {
//                        	System.out.println("2-4: text �씫�뼱�삤湲�");
                        	genreAlt = xpp.getText();
                            inGenreAlt = false;
                        }

                        break;

                    case XmlPullParser.END_TAG:		//3. �쁺�솕 1媛쒓� �걹�굹�뒗 �깭洹�(movie)瑜� 留뚮굹硫� �엫�떆蹂��닔�씠 �엳�뒗 媛믪쓣 紐쎈븙 �겢�옒�뒪�뿉 �꽔怨� list諛곗뿴�뿉 異붽��븿
//                    	System.out.println("3: end tag");
                        tag = xpp.getName();    //�뀒洹� �씠由� �뼸�뼱�삤湲�
//                        System.out.println("tag: "+tag+"\n==============");
                        if (tag.equals("movie")) {
//                        	System.out.println("3-1: 由ъ뒪�듃�뿉 �떞湲�\n********************************************************");
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
    }*/
}
