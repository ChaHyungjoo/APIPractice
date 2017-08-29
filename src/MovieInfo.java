
public class MovieInfo {
	private String movieNm;			//영화명(국문)
	private String movieNmEn;			//영화명(영문)
	private String openDt;				//개봉연도
	private String nationAlt;			//제작국가
	private String genreAlt;			//장르
	private String directors;			//감독
	private String backdropImage;		//스틸컷(배경크기) 이미지
	private String posterImage;		//포스터 이미지
	
	
	public String getMovieNm() {
		return movieNm;
	}
	
	public void setMovieNm(String movieNm) {
		this.movieNm = movieNm;
	}
	
	public String getMovieNmEn() {
		return movieNmEn;
	}
	
	public void setMovieNmEn(String movieNmEn) {
		this.movieNmEn = movieNmEn;
	}
	
	public String getOpenDt() {
		return openDt;
	}
	
	public void setOpenDt(String openDt) {
		this.openDt = openDt;
	}
	
	public String getNationAlt() {
		return nationAlt;
	}
	
	public void setNationAlt(String nationAlt) {
		this.nationAlt = nationAlt;
	}
	
	public String getGenreAlt() {
		return genreAlt;
	}
	
	public void setGenreAlt(String genreAlt) {
		this.genreAlt = genreAlt;
	}
	
	public String getDirectors() {
		return directors;
	}
	
	public void setDirectors(String directors) {
		if(this.directors==null)
			this.directors = directors;
		else 
			this.directors = this.directors+" "+directors;
	}
	
	public String getBackdropImage() {
		return backdropImage;
	}
	
	public void setBackdropImage(String backdropImage) {
		this.backdropImage = "https://image.tmdb.org/t/p/w1920"+backdropImage;
	}
	
	public String getPosterImage() {
		return posterImage;
	}
	
	public void setPosterImage(String posterImage) {
		this.posterImage = posterImage;
	}
	
	@Override
	public String toString() {
		return "MovieInfo [movieNm=" + movieNm + ", movieNmEn=" + movieNmEn + ", openDt=" + openDt + ", nationAlt="
				+ nationAlt + ", genreAlt=" + genreAlt + ", directors=" + directors + ", backdropImage=" + backdropImage
				+ ", posterImage=" + posterImage + "]";
	}
	
}
