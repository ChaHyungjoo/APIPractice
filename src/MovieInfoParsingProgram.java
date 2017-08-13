import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class MovieInfoParsingProgram {
	
    //XmlPullParser를 이용하여 영화진응위원회 에서 제공하는 OpenAPI XML 파일 파싱하기(parsing)
    static ArrayList<MovieInfo> getXmlData(String keyword) throws IOException {
    	boolean intitle = false, indate = false, innation = false, ingenre = false;
        String title = "", date = "", nation = "", genre = "";
    	
    	ArrayList<MovieInfo> list = new ArrayList<MovieInfo>();
    	MovieInfo info;
    	//http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.xml?key=354c88719a60cd3da952a4be7dbf367e&movieNm=추격자
    	String key = "354c88719a60cd3da952a4be7dbf367e";
    	String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
        String apiURL;
        apiURL = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.xml?key="+key+"&movieNm="+encodedKeyword;

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
                
				switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();    //태그 이름 얻어오기
                        if (tag.equals("movieNm")) {
                            intitle = true;
                        }

                        if (tag.equals("prdtYear")) {
                            indate = true;
                        }

                        if (tag.equals("nationAlt")) {
                            innation = true;
                        }

                        if (tag.equals("genreAlt")) {
                            ingenre = true;
                        }

                        break;

                    case XmlPullParser.TEXT:
                        if (intitle) {
                            title = xpp.getText();
                            intitle = false;
                        }
                        if (indate) {
                            date = xpp.getText();
                            indate = false;
                        }
                        if (innation) {
                            nation = xpp.getText();
                            innation = false;
                        }
                        if (ingenre) {
                            genre = xpp.getText();
                            ingenre = false;
                        }

                        break;

                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();    //테그 이름 얻어오기
                        if (tag.equals("movie")) {
                        	info = new MovieInfo();
                            info.setTitle(title);
                            info.setDate(date);
                            info.setNation(nation);
                            info.setGenre(genre);
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

	public static void main(String[] args) throws IOException {
		ArrayList<MovieInfo> list;
		
		list = MovieInfoParsingProgram.getXmlData("타이타닉");
		
		for(MovieInfo m: list)
			System.out.println(m.toString());

	}

}
