package api.jdbc;

import java.io.IOException;
import java.util.ArrayList;

public class KeybJdbcProgram {

	public static void main(String[] args) {
		ArrayList<Movie> movieList = new ArrayList<>();
		MovieInfo movieInfo = new MovieInfo();
		KeybMovieJdbc jdbc = new KeybMovieJdbc();
		
		String title = "타이타닉";
		
		try {
			
			movieList = movieInfo.movieData(title);
			//movieList = movieInfo.movieData();
			
			//int n = jdbc.input(movieList);
			
			for(Movie m : movieList)
				System.out.println(m.toString());
			
			//System.out.println(n);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
