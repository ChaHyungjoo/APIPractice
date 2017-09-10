package api.jdbc;

import java.util.ArrayList;
import java.util.List;

public class Test {

	public static void main(String[] args) {
		/*Movie movie = new Movie();
		
		movie.setBackdropImage(null);
		
		System.out.println(movie.getBackdropImage());*/
		
		String test = "Dear Friends «Ç«£«¢ «Õ«ì«ó«º";
		String test2 = "Dear Friends «Ç«£«¢ «Õ«ì«ó«º";
		if(test.equals(test2)) {
			
			System.out.println(test);
			System.out.println(test2);
		}
		
		

	}

}
