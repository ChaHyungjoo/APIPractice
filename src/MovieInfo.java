
public class MovieInfo {
	private String movieCd;			//��ȭ�ڵ�(��ȭ��������ȸ�� ��ϵ� ��ȭ)
	private String movieNm;			//��ȭ��(����)
	private String movieNmEn;		//��ȭ��(����)
	private String showTm;			//�󿵽ð�
	private String openDt;			//��������
	private String nationAlt;		//���۱���
	private String genreAlt;		//�帣
	private String directors;		//����
	private String actors;			//���
	private String backdropImage;	//��ƿ��(���ũ��) �̹���
	private String posterImage;		//������ �̹���
	
	
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
			this.directors = this.directors+directors;
	}
	
	public String getActors() {
		return actors;
	}

	public void setActors(String actors) {
		this.actors = actors;
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
		return "MovieInfo [movieCd=" + movieCd + ", movieNm=" + movieNm + ", movieNmEn=" + movieNmEn + ", showTm="
				+ showTm + ", openDt=" + openDt + ", nationAlt=" + nationAlt + ", genreAlt=" + genreAlt + ", directors="
				+ directors + ", actors=" + actors + ", backdropImage=" + backdropImage + ", posterImage=" + posterImage
				+ "]";
	}
	
}
