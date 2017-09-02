import java.io.IOException;
import java.util.ArrayList;

import org.json.*;

public class JsonProgram2 {

	public static void main(String[] args) {
		
		MovieInfoParsing2 parsing = new MovieInfoParsing2();
		ArrayList<MovieInfo> list = new ArrayList<>();
		
		String keyword = "미니언즈";
		
		try {
			list = parsing.movieData(keyword);
			
			for (MovieInfo m : list)
				System.out.println(m.toString());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
