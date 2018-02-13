package pkg;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.CrudRepository;


import pkg.Movie2;


	public interface MovieRepository extends CrudRepository<Movie2, Long> {
		Collection<Movie2> findBymovieid(String movie_id);
		Movie2 findOneBymovieid(String movie_id);
		Movie2 findOneBytitle(String title);
		Collection<Movie2> findBytitle(String title);
	}
