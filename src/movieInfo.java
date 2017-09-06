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

public class movieInfo {
	
	//��ȭ��������ȸ ���� �����ϴ� OpenAPI JSON ���� �Ľ�, ��ȭ�ڵ�(movieCd) + ������ + �̹��� �ٿ��� ����Ʈ �����ؼ� ��ȯ
	public ArrayList<Movie> movieData(String keyword) throws IOException {
		ArrayList<Movie> list = new ArrayList<>();
		Movie movie;
		
		String json = "";
		BufferedReader br;
		
		String key = "354c88719a60cd3da952a4be7dbf367e";
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
	
	
	//������ ��ȭ�ڵ�(movieCd)�� ��ȭ ������ + ���̹� ��ȭ ������ �̹��� + TMDB ��ȭ ��ƿ�� �̹��� ���̱�
	public Movie movieDataDetail(Movie movie, String movieCd) throws IOException {
		String json = "";
		BufferedReader br;
		
		String key = "354c88719a60cd3da952a4be7dbf367e";
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

	
	//TMDB���� �����ϴ� OpenAPI JSON ������ String���� ��ȯ�Ͽ� ��ȯ
	public Movie tmdbMovieData(Movie movie, String prdtYear, String movieNmEn) throws IOException {
		String json = "";
		BufferedReader br;
		
		String key = "fc26b37628734575187d1be55c6a3c85";
		
		String keyword = movieNmEn.toLowerCase();
		if(keyword.length()==0)
        	keyword = movie.getMovieNm();
		//System.out.println(keyword);
    	String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
        String apiURL;
        
        apiURL = "https://api.themoviedb.org/3/search/movie?"
        		+ "api_key=" + key + "&language=ko&query=" + encodedKeyword 
        		+ "&include_adult=false";	//�ش翬���� ��ȭ�� �˰������ �ǳ��� &year=2017 ������ �߰�
        
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
				//String release_date = entity.optString("release_date");
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
			/*
			movie.setMovieId(entity.getInt("id"));
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
	
	
	
	/*	//Naver���� �����ϴ� OpenAPI JSON ������ String���� ��ȯ�Ͽ� ��ȯ
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
        connection.setRequestProperty("X-Naver-Client-Id", clientId); //�߱޹���ID
        connection.setRequestProperty("X-Naver-Client-Secret", clientSecret);//�߱޹���PW
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
/*    //XmlPullParser�� �̿��Ͽ� ��ȭ��������ȸ ���� �����ϴ� OpenAPI XML ���� �Ľ��ϱ�(parsing)
    public ArrayList<MovieInfo> getXmlData(String keyword) throws IOException {
    	boolean inMovieNm = false, inOpenDt = false, inNationAlt = false, inGenreAlt = false;
        String movieNm = "", openDt = "", nationAlt = "", genreAlt = "";
    	
    	ArrayList<MovieInfo> list = new ArrayList<MovieInfo>();
    	MovieInfo info;
    	//http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.xml?key=354c88719a60cd3da952a4be7dbf367e&movieNm=�߰���
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
            xpp.setInput(connection.getInputStream(), "UTF-8");  //xml �Է¹ޱ�

            String tag;
            xpp.next();
            
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
//                System.out.println("\n while");
				switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:		//1. start�±׸� �����鼭 �ش��±׸� ���� flag�� true�� �ٲ���
//                    	System.out.println("1: start tag");
                        tag = xpp.getName();    //�±� �̸� ������
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

                    case XmlPullParser.TEXT:		//2. �±��� text�� �ӽú����� �־���
//                    	System.out.println("2: text");
                        if (inMovieNm) {
//                        	System.out.println("2-1: text �о����");
                        	movieNm = xpp.getText();
                            inMovieNm = false;
                        }
                        if (inOpenDt) {
//                        	System.out.println("2-2: text �о����");
                        	openDt = xpp.getText();
                            inOpenDt = false;
                        }
                        if (inNationAlt) {
//                        	System.out.println("2-3: text �о����");
                        	nationAlt = xpp.getText();
                            inNationAlt = false;
                        }
                        if (inGenreAlt) {
//                        	System.out.println("2-4: text �о����");
                        	genreAlt = xpp.getText();
                            inGenreAlt = false;
                        }

                        break;

                    case XmlPullParser.END_TAG:		//3. ��ȭ 1���� ������ �±�(movie)�� ������ �ӽú����� �ִ� ���� ���� Ŭ������ �ְ� list�迭�� �߰���
//                    	System.out.println("3: end tag");
                        tag = xpp.getName();    //�ױ� �̸� ������
//                        System.out.println("tag: "+tag+"\n==============");
                        if (tag.equals("movie")) {
//                        	System.out.println("3-1: ����Ʈ�� ���\n********************************************************");
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
