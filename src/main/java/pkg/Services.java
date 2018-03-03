package pkg;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import pkg.Movie2;
import pkg.MovieRepository;;


@Controller
@RequestMapping(path="/")
public class Services {
	@Autowired
	private MovieRepository movieModel;
	@Autowired
	RestTemplate temp;
	private static final String urlID="http://www.omdbapi.com/?i=%s&apikey=29aac7aa";
	private static final String urlTITLE="http://www.omdbapi.com/?t=%s&apikey=29aac7aa";
	
	private static final int BUTTONS_TO_SHOW = 5;
	private static final int INITIAL_PAGE = 0;
	private static final int INITIAL_PAGE_SIZE = 5;
	private static final int[] PAGE_SIZES = { 5, 10, 15 };

	@Autowired
	private MoviePaginationService movieService;
	
	@Autowired
	private MovieRepositoryServices movieRepositoryService;
	
	@Autowired
	private HibernateSearchService hibernateSearchService;
	
	public Services(MoviePaginationService service) {
		this.movieService=service;
	}
	
	@GetMapping(path="")
	public ModelAndView redirect() {
		return new ModelAndView("redirect:/list");
	}
	
	@GetMapping(path="/all")
	public @ResponseBody List<Movie2> findall() {
		return movieModel.findAll();
	}
	
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
	
	public Movie2 findMovieOMDB(String movieid) {
		String dest_url=String.format(urlID,movieid); 
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
	
	@RequestMapping("/helo")
	public String helo(@RequestParam(value="name", required=false, defaultValue="World") String name,Model model) {
		model.addAttribute("name", name);
		return "helo";
	}
	
	@RequestMapping("/list_old")
	public String htmlList(Model model) {
		model.addAttribute("list", movieModel.findAll());
		return "htmlListold";
	}

	@RequestMapping("/list")
	public ModelAndView htmlList(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page) {
		
		ModelAndView modelAndView = new ModelAndView("htmlList");
		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
		Page<Movie2> movies = movieService.findAllPageable(new PageRequest(evalPage, evalPageSize));
		Pager pager = new Pager(movies.getTotalPages(), movies.getNumber(), BUTTONS_TO_SHOW);
		modelAndView.addObject("list", movies);
		modelAndView.addObject("selectedPageSize", evalPageSize);
		modelAndView.addObject("pageSizes", PAGE_SIZES);
		modelAndView.addObject("pager", pager);		
		
		return modelAndView;
	}	
	
	//find titles in database
	@RequestMapping("/findTitleold")
	public @ResponseBody ModelAndView findListByTitle(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer>page,@RequestParam("title") String title) {
		System.out.println(title);
		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
		Page<Movie2> movies = movieService.findByTitleStartingWith(title.toLowerCase(),new PageRequest(evalPage, evalPageSize));
		ModelAndView modelAndView=new ModelAndView("findTitle");
		Pager pager = new Pager(movies.getTotalPages(), movies.getNumber(), BUTTONS_TO_SHOW);		
		modelAndView.addObject("list",movies);
		modelAndView.addObject("selectedPageSize",evalPageSize);
		modelAndView.addObject("pageSizes", PAGE_SIZES);
		modelAndView.addObject("pager", pager);			
		return modelAndView;
	}

	//findTitle in hibernate's index
	@RequestMapping("/findTitle")
	public @ResponseBody ModelAndView searchListByTitle(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer>page,@RequestParam("title") String title) {
		System.out.println(title);
		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
		Page<Movie2> movies = hibernateSearchService.titleIdSearchByPage(title, evalPage, evalPageSize);
		ModelAndView modelAndView=new ModelAndView("findTitle");
		Pager pager = new Pager(movies.getTotalPages(), movies.getNumber(), BUTTONS_TO_SHOW);		
		modelAndView.addObject("list",movies);
		modelAndView.addObject("selectedPageSize",evalPageSize);
		modelAndView.addObject("pageSizes", PAGE_SIZES);
		modelAndView.addObject("pager", pager);			
		return modelAndView;
	}	
	
	
	@RequestMapping("/finder")
	public @ResponseBody List<Movie2> findTitleId(@RequestParam("searched")String titleId){
		try {
			System.out.println(titleId);
			List<Movie2> movies= hibernateSearchService.titleIdSearch(titleId);
			if(movies.size()>0) {
				return movies;
			}
			else {
				return null;
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	//find title in database
	@RequestMapping("/findTitleold/jsonPage")
	public @ResponseBody Page<Movie2> findListByTitleJson(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer>page,@RequestParam("title") String title) {
		System.out.println(title);
		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
		Page<Movie2> movies = movieService.findByTitleStartingWith(title.toLowerCase(),new PageRequest(evalPage, evalPageSize));
		ModelAndView modelAndView=new ModelAndView("findTitle");
		Pager pager = new Pager(movies.getTotalPages(), movies.getNumber(), BUTTONS_TO_SHOW);		
		return movies;
	}	

	//find title in hibernate's index
	@RequestMapping("/movies/{title}")
	public @ResponseBody Page<Movie2> searchListByTitleJson(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer>page,@PathVariable("title") String title) {
		System.out.println(title);
		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
		Page<Movie2> movies = hibernateSearchService.titleIdSearchByPage(title, evalPage, evalPageSize);
		return movies;
	}	
	
	
	@GetMapping(path="/movies",produces = "application/json")
	public @ResponseBody Page<Movie2> jsonPage(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page) {
		ModelAndView modelAndView = new ModelAndView("htmlList");
		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
		Page<Movie2> movies = movieService.findAllPageable(new PageRequest(evalPage, evalPageSize));
		Pager pager = new Pager(movies.getTotalPages(), movies.getNumber(), BUTTONS_TO_SHOW);
		return movies;
	}	
	
	@GetMapping(path="/one",produces = "application/json")
	public @ResponseBody List<Movie2> saveofrequest(@RequestParam String movieid) {
		
		return movieRepositoryService.findBymovieid(movieid);
	}	
	
	private Movie2 getMovieById(String movieid) {
		return movieRepositoryService.findOneBymovieid(movieid);
	}

	private Movie2 getMovieByTitle(String title) {
		return movieRepositoryService.findMovieByTitle(title);
	}
	
	private boolean validarMoviePorNombre(String title) {
		List<Movie2> movies= movieRepositoryService.findMoviesByTitle(title);
		System.out.println("SIZE:"+movies.size());
		if(movies.size()>0) {
			return true;
		}
		return false;
	}	
	
	private boolean validarMovie(String movie_id) {
		List<Movie2> movies=movieRepositoryService.findBymovieid(movie_id);
		if(movies.size()>0) {
			return true;
		}
		return false;
	}
	
	@RequestMapping(method = RequestMethod.GET,path="/edit",produces = "application/json")
	public String edit(@RequestParam String movieid, Model model) {
		Movie2 finded=movieRepositoryService.findMovieById(movieid); 
		model.addAttribute("movie",finded);
		return "edit";
	}
	
	@RequestMapping(method = RequestMethod.GET,path="/find",produces = "text/html")
	public String newMovie() {
		 return "find";
	}	

	@RequestMapping(method = RequestMethod.POST,path="/add",produces = "text/html")
	public String addMovie(@RequestParam String movieid,@RequestParam("optionsRadios")String[] checkboxValue, Model model) {
		System.out.println(movieid);
		System.out.println(checkboxValue[0].toString());
		if(checkboxValue[0].equals("id")) {
			System.out.println("POR ID");
			if(this.validarMovie(movieid)) {
				Movie2 find=getMovieById(movieid);
				model.addAttribute("movie",find);
				model.addAttribute("existinDB","Existe en base de datos");
				return "add";
			}
			else {
				Movie2 newMovie=findMovieOMDB(movieid);
				if(newMovie!=null) {
					model.addAttribute("movie",newMovie);
					model.addAttribute("existinDB","Listo para agregar");
					return "add";
				}
				model.addAttribute("status","No existe ID");
				return "find";
			}
		}
		else {
			System.out.println("POR NOMBRE");
			if(this.validarMoviePorNombre(movieid)) {
				Movie2 find=getMovieByTitle(movieid);
				model.addAttribute("movie",find);
				model.addAttribute("existinDB","Existe en base de datos");
				return "add";
			}
			else {
				Movie2 newMovie=findMovieOMDBByTitle(movieid);
				if(newMovie!=null) {
					model.addAttribute("movie",newMovie);
					model.addAttribute("existinDB","Listo para agregar");
					return "add";
				}
				model.addAttribute("status","No existe ID");
				return "find";
			}
		}
	}	

	@PutMapping(path="/api/movies/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> updateMovie(@Valid @RequestBody Movie2 movie,@PathVariable String id) {
		System.out.println(movie.getTitle());
		movie.setMovieid(id);
		movieRepositoryService.saveMovie(movie);
		URI location=ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PostMapping(path="/api/movies")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> addMovie(@RequestBody Movie2 movie) {
		movieRepositoryService.saveMovie(movie);
		URI location=ServletUriComponentsBuilder
				.fromCurrentRequest().replacePath("/movie/{id}")
				.buildAndExpand(movie.getMovieid()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@DeleteMapping(path="/api/movies/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteMovie(@PathVariable String id) {
		movieRepositoryService.deleteMovie(id);
	}
	
}
