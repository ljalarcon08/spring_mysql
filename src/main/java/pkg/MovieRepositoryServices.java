package pkg;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovieRepositoryServices {
	
	@Autowired
	public MovieRepository movieRepository;
	
	public void saveMovie(Movie2 movie) {
		movieRepository.save(movie);
	}
	
	@Transactional(readOnly=true)
	public List<Movie2> findAll(){
		return movieRepository.findAll();
	}
	
	@Transactional(readOnly=true)
	public Movie2 findOneBymovieid(String movieid) {
		return movieRepository.findOneBymovieid(movieid);
	}
	
	@Transactional(readOnly=true)
	public List<Movie2> findBymovieid(String movieid){
		return movieRepository.findBymovieid(movieid);
	}
	
	@Transactional(readOnly=true)
	public Movie2 findMovieById(String movieid) {
		return movieRepository.findOneBymovieid(movieid);
	}
	
	@Transactional(readOnly=true)
	public Movie2 findMovieByTitle(String title) {
		return movieRepository.findOneBytitle(title);
	}
	
	@Transactional(readOnly=true)
	public List<Movie2> findMoviesByTitle(String title){
		return movieRepository.findBytitle(title);
	}
	
	@Transactional
	public void deleteMovie(String movieid) {
		 movieRepository.delete(movieid);
	}
}
