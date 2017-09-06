import java.io.IOException;
import java.util.ArrayList;

import org.json.*;

public class MovieProgram {

	public static void main(String[] args) {
		
		movieInfo movieInfo = new movieInfo();
		ArrayList<Movie> list = new ArrayList<>();
		
		String keyword = "±è±¤¼®";
		
		try {
			list = movieInfo.movieData(keyword);
			
			for (Movie m : list)
				System.out.println(m.toString());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
