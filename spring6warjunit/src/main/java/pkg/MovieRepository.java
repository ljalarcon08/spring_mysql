package pkg;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import pkg.Movie2;



	// This will be AUTO IMPLEMENTED by Spring into a Bean
	// CRUD refers Create, Read, Update, Delete

	public interface MovieRepository extends CrudRepository<Movie2, Long> {
		Collection<Movie2> findBymovieid(String movie_id);
		Movie2 findOneBymovieid(String movie_id);
		Movie2 findOneBytitle(String title);
		Collection<Movie2> findBytitle(String title);
	}
