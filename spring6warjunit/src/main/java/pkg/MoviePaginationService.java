package pkg;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MoviePaginationService {
	Page<Movie2> findAllPageable(Pageable pageable);
}
