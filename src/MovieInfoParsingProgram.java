import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class MovieInfoParsingProgram {
	
    //XmlPullParser�� �̿��Ͽ� ��ȭ��������ȸ ���� �����ϴ� OpenAPI XML ���� �Ľ��ϱ�(parsing)
    static ArrayList<MovieInfo> getXmlData(String keyword) throws IOException {
    	boolean inTitle = false, inDate = false, inNation = false, inGenre = false;
        String title = "", date = "", nation = "", genre = "";
    	
    	ArrayList<MovieInfo> list = new ArrayList<MovieInfo>();
    	MovieInfo info;
    	//http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.xml?key=354c88719a60cd3da952a4be7dbf367e&movieNm=�߰���
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

                    case XmlPullParser.TEXT:		//2. �±��� text�� �ӽú����� �־���
//                    	System.out.println("2: text");
                        if (inTitle) {
//                        	System.out.println("2-1: text �о����");
                            title = xpp.getText();
                            inTitle = false;
                        }
                        if (inDate) {
//                        	System.out.println("2-2: text �о����");
                            date = xpp.getText();
                            inDate = false;
                        }
                        if (inNation) {
//                        	System.out.println("2-3: text �о����");
                            nation = xpp.getText();
                            inNation = false;
                        }
                        if (inGenre) {
//                        	System.out.println("2-4: text �о����");
                            genre = xpp.getText();
                            inGenre = false;
                        }

                        break;

                    case XmlPullParser.END_TAG:		//3. ��ȭ 1���� ������ �±�(movie)�� ������ �ӽú����� �ִ� ���� ���� Ŭ������ �ְ� list�迭�� �߰���
//                    	System.out.println("3: end tag");
                        tag = xpp.getName();    //�ױ� �̸� ������
//                        System.out.println("tag: "+tag+"\n==============");
                        if (tag.equals("movie")) {
//                        	System.out.println("3-1: ����Ʈ�� ���\n********************************************************");
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
		
		list = MovieInfoParsingProgram.getXmlData("�ýÿ�����");
		
		for(MovieInfo info: list)
			System.out.println(info.toString());

	}

}
