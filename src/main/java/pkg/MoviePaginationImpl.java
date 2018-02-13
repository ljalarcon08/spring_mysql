package pkg;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MoviePaginationImpl implements MoviePaginationService{
	
	private MoviePaginationRepo moviePagRepo;
	
	public MoviePaginationImpl(MoviePaginationRepo moviePagRepo) {
		this.moviePagRepo=moviePagRepo;
	}
	
	@Transactional
	@Override
	public Page<Movie2> findAllPageable(Pageable pageable) {
		return moviePagRepo.findAll(pageable);
	}

	@Override
	public Page<Movie2> findByTitleStartingWith(String lowerCase, Pageable pageable) {
		return moviePagRepo.findByTitleStartingWith(lowerCase,pageable);
	}

}
