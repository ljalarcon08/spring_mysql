package pkg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DataLoader implements ApplicationRunner{

	private MovieRepository movieModel;
	private RestTemplate temp;
	private static final String urlTITLE="http://www.omdbapi.com/?t=%s&apikey=29aac7aa";
	
	@Autowired
    public DataLoader(MovieRepository movieRepository) {
        this.movieModel = movieRepository;
    }
	
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		//System.out.println(movieModel.count());
		
		if(this.movieModel.count()==0) {

			if(this.movieModel.count()==0) {
				String movies[]= {"The good","El","Ocho","matrix","back to","shark","alien","The god","The","The silence","The pianist","The lion","The Prestige"};
				for(int i=0;i<movies.length;i++) {
					//System.out.println(movies[i]);
					Movie2 movieToAdd=this.findMovieOMDBByTitle(movies[i]);
					movieModel.save(movieToAdd);
				}
			}
		}
		
	}

	public Movie2 findMovieOMDBByTitle(String title) {
		String dest_url=String.format(urlTITLE,title); 
		System.out.println(dest_url);
		temp=restTemplate();
		JMovie mov=temp.getForObject(dest_url,JMovie.class);
		if(Boolean.parseBoolean(mov.getResponse())) {
			Movie2 newMovie=new Movie2();
			newMovie.setMovieid(mov.getImdbID());
			newMovie.setTitle(mov.getTitle());
			newMovie.setRelease_year(mov.getYear());
			newMovie.setRating(mov.getImdbRating());
			newMovie.setImage(mov.getPoster());
			return newMovie;
		}
		return null;
	}	
	
}
