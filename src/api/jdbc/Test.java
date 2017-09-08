package api.jdbc;

public class Test {

	public static void main(String[] args) {
		Movie movie = new Movie();
		
		movie.setBackdropImage(null);
		
		System.out.println(movie.getBackdropImage());
		

	}

}
