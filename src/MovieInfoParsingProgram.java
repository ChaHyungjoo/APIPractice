import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class MovieInfoParsingProgram {

	public static void main(String[] args) throws IOException {
		
		MovieInfoParsing parsing = new MovieInfoParsing();
		
/*		ArrayList<MovieInfo> list;
		
		list = parsing.getXmlData("택시운전사");
		
		for(MovieInfo info: list)
			System.out.println(info.toString());*/
		
		String json =  parsing.KOBISJsonData("택시운전사");
		
		System.out.println(json);

	}

}
