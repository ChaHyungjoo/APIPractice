import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class ImgParsingProgram {
   
    //영화이미지를 가져오기 위한 naver open api 메소드
    static String naverXmlData(String title) throws IOException {
    	String imgUrl = "";
    	String replacedImgUrl = "";
        String clientId = "aVyrhY81Hji8r3ApgQzx";
        String clientSecret = "e5vXcLz5J9";
        String text = URLEncoder.encode(title, "UTF-8");
        String apiURL = "https://openapi.naver.com/v1/search/movie.xml?query=" + text + 
        				"&start=1&display=80";  //영화 api

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
                        System.out.println(tag);
                        if (tag.equals("item")) ;// 첫번째 검색결과

                        else if (tag.equals("link")) {
                            xpp.next();
                            imgUrl = xpp.getText();
                        }

                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();    //테그 이름 얻어오기
                        if (tag.equals("item"))
                            break;
                }

                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        return imgUrl;
//        replacedImgUrl = imgUrl.replace("basic.nhn?code=", "photoViewPopup.nhn?movieCode=");
//        return replacedImgUrl;
    }
   public static void main(String[] args) throws IOException {
      String str = ImgParsingProgram.naverXmlData("택시운전사");
	   
      System.out.println(str);

   }

}