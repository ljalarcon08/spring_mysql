package pkg;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoviePaginationRepo extends PagingAndSortingRepository<Movie2,Long>{

	Page<Movie2> findByTitleStartingWith(String lowerCase, Pageable pageable);

}
