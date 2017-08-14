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
    	boolean inTitle = false, inDate = false, inNation = false, inGenre = false;
        String title = "", date = "", nation = "", genre = "";
    	
    	ArrayList<MovieInfo> list = new ArrayList<MovieInfo>();
    	MovieInfo info;
    	//http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.xml?key=354c88719a60cd3da952a4be7dbf367e&movieNm=추격자
    	String key = "354c88719a60cd3da952a4be7dbf367e";
    	String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
        String apiURL;
        apiURL = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.xml?key="
        		+key+"&itemPerPage=1000&movieNm="+encodedKeyword;

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
                            inTitle = true;
                        }

                        if (tag.equals("prdtYear")) {
                            inDate = true;
                        }

                        if (tag.equals("nationAlt")) {
                            inNation = true;
                        }

                        if (tag.equals("genreAlt")) {
                            inGenre = true;
                        }

                        break;

                    case XmlPullParser.TEXT:		//2. 태그의 text를 임시변수에 넣어줌
//                    	System.out.println("2: text");
                        if (inTitle) {
//                        	System.out.println("2-1: text 읽어오기");
                            title = xpp.getText();
                            inTitle = false;
                        }
                        if (inDate) {
//                        	System.out.println("2-2: text 읽어오기");
                            date = xpp.getText();
                            inDate = false;
                        }
                        if (inNation) {
//                        	System.out.println("2-3: text 읽어오기");
                            nation = xpp.getText();
                            inNation = false;
                        }
                        if (inGenre) {
//                        	System.out.println("2-4: text 읽어오기");
                            genre = xpp.getText();
                            inGenre = false;
                        }

                        break;

                    case XmlPullParser.END_TAG:		//3. 영화 1개가 끝나는 태그(movie)를 만나면 임시변수이 있는 값을 몽땅 클래스에 넣고 list배열에 추가함
//                    	System.out.println("3: end tag");
                        tag = xpp.getName();    //테그 이름 얻어오기
//                        System.out.println("tag: "+tag+"\n==============");
                        if (tag.equals("movie")) {
//                        	System.out.println("3-1: 리스트에 담기\n********************************************************");
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
		
		list = MovieInfoParsingProgram.getXmlData("택시운전사");
		
		for(MovieInfo info: list)
			System.out.println(info.toString());

	}

}
