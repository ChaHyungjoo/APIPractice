import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class MovieInfoParsing {
	
	//TMDB���� �����ϴ� OpenAPI JSON ������ String���� ��ȯ�Ͽ� ��ȯ
	public String TMDBJsonData(String keyword) throws IOException {
		String json = "";
		BufferedReader br;
		
		String key = "fc26b37628734575187d1be55c6a3c85";
    	String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
        String apiURL;
        
        apiURL = "https://api.themoviedb.org/3/search/movie?"
        		+ "api_key=" + key + "&query=" + encodedKeyword + "&include_adult=false";	//�ش翬���� ��ȭ�� �˰������ �ǳ��� &year=2017 ������ �߰�
        
        URL url = new URL(apiURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        
        br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
		
		json = br.readLine();
		
		return json;
	}
	
	//Naver���� �����ϴ� OpenAPI JSON ������ String���� ��ȯ�Ͽ� ��ȯ
	public String NaverJsonData(String keyword) throws IOException {
		String _json = "";
		BufferedReader br;
		
		String clientId = "aVyrhY81Hji8r3ApgQzx";
        String clientSecret = "e5vXcLz5J9";
        String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
		
        String apiURL = "https://openapi.naver.com/v1/search/movie.json?query=" + encodedKeyword + "&display=100";
		
        URL url = new URL(apiURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-Naver-Client-Id", clientId); //�߱޹���ID
        connection.setRequestProperty("X-Naver-Client-Secret", clientSecret);//�߱޹���PW
//        connection.setRequestProperty("Content-Type", "application/json");
        
        br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
        
        while((_json=_json+br.readLine()) != null) {
        	if(_json.contains("null"))
        		break;
        }
        
        String json = _json.replace("null", "");
        
		return json;
	}
	
	
	//��ȭ��������ȸ ���� �����ϴ� OpenAPI JSON ������ String���� ��ȯ�Ͽ� ��ȯ
	public String KOBISJsonData(String keyword) throws IOException {
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
        
		return json;
	}
	
	
    //XmlPullParser�� �̿��Ͽ� ��ȭ��������ȸ ���� �����ϴ� OpenAPI XML ���� �Ľ��ϱ�(parsing)
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
                            info.setNationAlt(nationAlt);
                            info.setGenreAlt(genreAlt);
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
