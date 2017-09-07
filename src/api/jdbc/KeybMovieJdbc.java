package api.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KeybMovieJdbc {

	public int input(ArrayList<Movie> movieList) {
		
		int result = 0;
		String sql = "insert into MovieTest values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String url = "jdbc:mysql://211.238.142.247/keybdb?autoReconnect=true&amp;useSSL=false&characterEncoding=UTF-8";
		
		for(int i=0; i<movieList.size(); i++) {
			// JDBC 드라이버 로드
			  try {
				  Class.forName("com.mysql.jdbc.Driver");
				
				   // 연결 / 인증
			      Connection con = DriverManager.getConnection(url, "keyb", "keyb");
			
			      // 실행
			      PreparedStatement st = con.prepareStatement(sql);
			      st.setInt(1, movieList.get(i).getMovieCd());
			      st.setString(2, movieList.get(i).getMovieNm());
			      st.setString(3, movieList.get(i).getMovieNmEn());
			      st.setString(4, movieList.get(i).getShowTm());
			      st.setString(5, movieList.get(i).getPrdtYear());
			      st.setString(6, movieList.get(i).getOpenDt());
			      st.setString(7, movieList.get(i).getNations());
			      st.setString(8, movieList.get(i).getGenres());
			      st.setString(9, movieList.get(i).getDirectors());
			      st.setString(10, movieList.get(i).getActors());
			      st.setString(11, movieList.get(i).getWatchGradeNm());
			      st.setInt(12, movieList.get(i).getMovieId());
			      //st.setString(12, movieList.get(i).getMovieCd());
			      st.setString(13, movieList.get(i).getBackdropImage());
			      st.setString(14, movieList.get(i).getPosterImage());
			      st.setString(15, movieList.get(i).getOverView());
			      st.setString(16, movieList.get(i).getTrailer());
			      
			      result += st.executeUpdate();
			      
			      st.close();
			      con.close();
			      
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		return result;
	}

}
