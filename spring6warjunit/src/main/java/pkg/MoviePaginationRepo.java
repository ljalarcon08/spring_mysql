package pkg;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoviePaginationRepo extends PagingAndSortingRepository<Movie2,Long>{

}
