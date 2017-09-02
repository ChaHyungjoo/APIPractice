
public class MovieInfo {
	//----영화진흥위원회(kobis)에서 가져오는 부분-----------------------------
	private String movieCd;			//영화코드(영화진흥위원회에 등록된 영화)
	private String movieNm;			//영화명(국문)
	private String movieNmEn;		//영화명(영문)
	private String showTm;			//상영시간
	private String prdtYear;		//제작연도
	private String openDt;			//개봉연도
	private String nations;			//제작국가
	private String genres;			//장르
	private String directors;		//감독
	private String actors;			//배우
	private String watchGradeNm;	//관람등급
	
	//----tmdb에서 가져오는 부분-----------------------------
	private int movieId;			//tmdb의 영화 코드(id)
	private String backdropImage;	//스틸컷(배경크기) 이미지
	private String posterImage;		//포스터 이미지
	private String overView;		//줄거리
	private String trailer;			//예고편
	

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}

	public String getMovieCd() {
		return movieCd;
	}

	public void setMovieCd(String movieCd) {
		this.movieCd = movieCd;
	}

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
	
	public String getShowTm() {
		return showTm;
	}

	public void setShowTm(String showTm) {
		this.showTm = showTm;
	}

	public String getPrdtYear() {
		return prdtYear;
	}

	public void setPrdtYear(String prdtYear) {
		this.prdtYear = prdtYear;
	}

	public String getOpenDt() {
		return openDt;
	}
	
	public void setOpenDt(String openDt) {
		this.openDt = openDt;
	}
	
	public String getNations() {
		return nations;
	}
	
	public void setNations(String nations) {
		if(this.nations==null)
			this.nations = nations;
		else 
			this.nations = this.nations + nations;
	}
	
	public String getGenres() {
		return genres;
	}
	
	public void setGenres(String genres) {
		if(this.genres==null)
			this.genres = genres;
		else 
			this.genres = this.genres + genres;
	}
	
	public String getDirectors() {
		return directors;
	}
	
	public void setDirectors(String directors) {
		if(this.directors==null)
			this.directors = directors;
		else 
			this.directors = this.directors + directors;
	}
	
	public String getActors() {
		return actors;
	}

	public void setActors(String actors) {
		if(this.actors==null)
			this.actors = actors;
		else 
			this.actors = this.actors + actors;
	}
	
	public String getWatchGradeNm() {
		return watchGradeNm;
	}

	public void setWatchGradeNm(String watchGradeNm) {
		this.watchGradeNm = watchGradeNm;
	}
	
	public int getMovieId() {
		return movieId;
	}

	public String getBackdropImage() {
		return backdropImage;
	}
	
	public void setBackdropImage(String backdropImage) {
		if(backdropImage.length()==0)
			this.backdropImage = null;
		else
			this.backdropImage = "https://image.tmdb.org/t/p/w1920"+backdropImage;
	}
	
	public String getPosterImage() {
		return posterImage;
	}
	
	public void setPosterImage(String posterImage) {
		if(posterImage.length()==0)
			this.posterImage = null;
		else
			this.posterImage = "https://image.tmdb.org/t/p/w1000"+posterImage;
	}
	
	/*	public void setPosterImage(String posterImage) {
		this.posterImage = posterImage;
	}*/
	
	public String getOverView() {
		return overView;
	}

	public void setOverView(String overView) {
		this.overView = overView;
	}

	public String getTrailer() {
		return trailer;
	}

	public void setTrailer(String trailer) {
		if(trailer.length()==0)
			this.trailer = null;
		else
			this.trailer = "https://www.youtube.com/watch?v="+trailer;
	}

	
	@Override
	public String toString() {
		return "MovieInfo [movieCd=" + movieCd + ", movieNm=" + movieNm + ", movieNmEn=" + movieNmEn + ", showTm="
				+ showTm + ", prdtYear=" + prdtYear + ", openDt=" + openDt + ", nations=" + nations + ", genres="
				+ genres + ", directors=" + directors + ", actors=" + actors + ", watchGradeNm=" + watchGradeNm
				+ ", movieId=" + movieId + ", backdropImage=" + backdropImage + ", posterImage=" + posterImage
				+ ", overView=" + overView + ", trailer=" + trailer + "]";
	}


}
