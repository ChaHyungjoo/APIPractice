package api.jdbc;

import java.io.IOException;
import java.util.ArrayList;

public class MovieJdbcProgram {

	public static void main(String[] args) {
		ArrayList<Movie> movieList = new ArrayList<>();
		MovieInfo movieInfo = new MovieInfo();
		MovieJdbc movieJdbc = new MovieJdbc();
		
		String title = "¾ÆÄí¾Æ";
		
		try {
			
			movieList = movieInfo.movieData(title);
			
			int n = movieJdbc.input(movieList);
			System.out.println(n);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
