package pkg;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.CrudRepository;


import pkg.Movie2;


	public interface MovieRepository extends CrudRepository<Movie2, String> {
		//Collection<Movie2> findBymovieid(String movie_id);
		List<Movie2> findBymovieid(String movie_id);
		Movie2 findOneBymovieid(String movie_id);
		Movie2 findOneBytitle(String title);
		List<Movie2> findBytitle(String title);
		List<Movie2> findAll();
	}
