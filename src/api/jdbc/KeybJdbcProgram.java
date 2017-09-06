package api.jdbc;

import java.io.IOException;
import java.util.ArrayList;

public class KeybJdbcProgram {

	public static void main(String[] args) {
		ArrayList<Movie> movieList = new ArrayList<>();
		MovieInfo movieInfo = new MovieInfo();
		KeybMovieJdbc jdbc = new KeybMovieJdbc();
		
		String title = "택시운전사";
		
		try {
			
			movieList = movieInfo.movieData(title);
			
			int n = jdbc.input(movieList);
			System.out.println(n);
			
			for(Movie m : movieList)
				System.out.println(m.toString());
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
