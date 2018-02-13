package pkg;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

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
	
	public Services(MoviePaginationService service) {
		this.movieService=service;
	}
	
	@GetMapping(path="/all")
	public @ResponseBody Iterable<Movie2> findall() {
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
	
	@GetMapping(path="/rest",produces = "application/json")
	public @ResponseBody String findone(@RequestParam String movieid) {
		Movie2 newMovie=findMovieOMDB(movieid);
		if(newMovie!=null) {
			movieModel.save(newMovie);
			return "{'msg':'UPT/INS'}";
		}
		return "{'msg':'Movie ID No existe'}";
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
	
	@RequestMapping("/findTitle")
	public @ResponseBody ModelAndView findListByTitle(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer>page,@RequestParam("title") String title) {
		//ModelAndView modelAndView=new ModelAndView("findList");
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

	@RequestMapping("/findTitle/jsonPage")
	public @ResponseBody Page<Movie2> findListByTitleJson(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer>page,@RequestParam("title") String title) {
		//ModelAndView modelAndView=new ModelAndView("findList");
		System.out.println(title);
		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
		Page<Movie2> movies = movieService.findByTitleStartingWith(title.toLowerCase(),new PageRequest(evalPage, evalPageSize));
		ModelAndView modelAndView=new ModelAndView("findTitle");
		Pager pager = new Pager(movies.getTotalPages(), movies.getNumber(), BUTTONS_TO_SHOW);		
		return movies;
	}	
	
	@GetMapping(path="/jsonPage",produces = "application/json")
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
	public @ResponseBody Iterable<Movie2> saveofrequest(@RequestParam String movieid) {
		
		return movieModel.findBymovieid(movieid);
	}	
	
	
	@GetMapping(path="/add",produces = "application/json")
	public @ResponseBody String addMovieGet (@RequestParam String movie_id
			, @RequestParam String image,@RequestParam String rating,
			@RequestParam String release_year,@RequestParam String title) {

		Movie2 movie = new Movie2();
		movie.setMovieid(movie_id);
		movie.setImage(image);
		movie.setTitle(title);
		movie.setRating(rating);
		movie.setRelease_year(release_year);
		movieModel.save(movie);
		return "Saved";
	}
	
	@RequestMapping(method = RequestMethod.POST,path="/add",produces = "application/json")
	public @ResponseBody String addMoviePost(@RequestBody Movie2 movie){
		if(!this.validarMovie(movie.getMovieid())) {
			movieModel.save(movie);
			return "{'msg':'ok'}";
		}
		else {
			return "{'msg':'error'}";
		}
	}
	
	@RequestMapping(method = RequestMethod.DELETE,path="/del",produces = "text/html")
	public @ResponseBody ModelAndView removeMovieList(@RequestParam String movieid){
		System.out.println(movieid);
		if(this.validarMovie(movieid)) {
			Movie2 todel=getMovieById(movieid);
			movieModel.delete(todel);
		}
		return new ModelAndView("redirect:/list");
	}		
	
	@RequestMapping(method = RequestMethod.DELETE,path="/delete",produces = "application/json")
	public @ResponseBody String removeMoviePost(@RequestParam String movieid){
		System.out.println(movieid);
		if(this.validarMovie(movieid)) {
			Movie2 todel=getMovieById(movieid);
			movieModel.delete(todel);
			return "{'msg':'ok'}";
		}
		else {
			return "{'msg':'error not found'}";
		}
	}	
	
	private Movie2 getMovieById(String movieid) {
		return movieModel.findOneBymovieid(movieid);
	}

	private Movie2 getMovieByTitle(String title) {
		return movieModel.findOneBytitle(title);
	}
	
	private boolean validarMoviePorNombre(String title) {
		Collection<Movie2> movies= this.movieModel.findBytitle(title);
		System.out.println("SIZE:"+movies.size());
		if(movies.size()>0) {
			return true;
		}
		return false;
	}	
	
	private boolean validarMovie(String movie_id) {
		Collection<Movie2> movies= this.movieModel.findBymovieid(movie_id);
		if(movies.size()>0) {
			return true;
		}
		return false;
	}
	
	@RequestMapping(method = RequestMethod.GET,path="/edit",produces = "application/json")
	public String edit(@RequestParam String movieid, Model model) {
		 Movie2 finded=movieModel.findBymovieid(movieid).iterator().next();
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
	
	@RequestMapping(method = RequestMethod.PUT,path="/upd",produces = "application/json")
	public @ResponseBody String updateMovie(@RequestBody Movie2 movie){
		if(!this.validarMovie(movie.getMovieid())) {
			movieModel.save(movie);
			return "{'msg':'insert ok'}";
		}
		else {
			movieModel.save(movie);
			return "{'msg':'update ok'}";
		}
	}	 
	
	@PutMapping(value="/update")
	public @ResponseBody ModelAndView updateMov(@RequestParam String movieid,@RequestParam String title,
			@RequestParam String release_year,@RequestParam String rating,@RequestParam String image){
		Movie2 updatedMovie=new Movie2();
		updatedMovie.setMovieid(movieid);
		updatedMovie.setTitle(title);
		updatedMovie.setRelease_year(release_year);
		updatedMovie.setRating(rating);
		updatedMovie.setImage(image);
		if(!this.validarMovie(updatedMovie.getMovieid())) {
			movieModel.save(updatedMovie);
			return new ModelAndView("redirect:/list");
		}
		else {
			movieModel.save(updatedMovie);
			return new ModelAndView("redirect:/list");
		}
	}	 

}
