import java.io.IOException;
import java.util.ArrayList;

import org.json.*;

public class JsonProgram2 {

	public static void main(String[] args) {
		
		MovieInfoParsing2 parsing = new MovieInfoParsing2();
		ArrayList<MovieInfo> list = new ArrayList<>();
		MovieInfo info;
		
		String json = "";
		String keyword = "πË∆Æ∏«";
		
		try {
			list = parsing.KOBISJsonData(keyword);
			
			for (MovieInfo m : list)
				System.out.println(m.toString());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
