import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class ImgParsingProgram {
   
    //영화이미지를 가져오기 위한 naver open api 메소드
    static ArrayList<MovieInfo> naverXmlData(String keyword) throws IOException {
    	ArrayList<MovieInfo> list = new ArrayList<>();
    	MovieInfo info;
    	boolean inImgUrl = false;
    	String imgUrl = "";
    	String replacedImgUrl = "";
    	int year = Calendar.getInstance().get(Calendar.YEAR);
    	
        String clientId = "aVyrhY81Hji8r3ApgQzx";
        String clientSecret = "e5vXcLz5J9";
        String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
        String apiURL = "https://openapi.naver.com/v1/search/movie.xml?query=" + encodedKeyword + 
        				"&start=1&display=100&yearfrom="+ year +"&yearto="+ year;  //영화 api
        
        

        URL url = new URL(apiURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-Naver-Client-Id", clientId); //발급받은ID
        connection.setRequestProperty("X-Naver-Client-Secret", clientSecret);//발급받은PW
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
                        if (tag.equals("link")) {
                        	inImgUrl = true;
                        }

                        break;

                    case XmlPullParser.TEXT:
                    	if (inImgUrl) {
                            imgUrl = xpp.getText();
                            replacedImgUrl = imgUrl.replace("basic.nhn?code=", "photoViewPopup.nhn?movieCode=");
                            inImgUrl = false;
                    	}
                    	
                        break;

                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();    //테그 이름 얻어오기
                        if (tag.equals("item")) {
                        	info = new MovieInfo();
                        	info.setImgLink(replacedImgUrl);
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
//        replacedImgUrl = imgUrl.replace("basic.nhn?code=", "photoViewPopup.nhn?movieCode=");
//        return replacedImgUrl;
    }
   public static void main(String[] args) throws IOException {
	   
	   ArrayList<MovieInfo> list = new ArrayList<>();
	   
	   list = naverXmlData("청년경찰");
	   
	   for(MovieInfo info: list)
		   System.out.println(info.getImgLink());
	   

   }

}