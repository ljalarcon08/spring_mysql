package pkg;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import net.minidev.json.JSONArray;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServicesTest {
	private static final int BUTTONS_TO_SHOW = 5;
	private static final int INITIAL_PAGE = 0;
	private static final int INITIAL_PAGE_SIZE = 5;
	private static final int[] PAGE_SIZES = { 5, 10, 15 };

	private TestRestTemplate rest;
	
	@Autowired
	private MovieRepository movieModel;
	
	@Bean
	public TestRestTemplate restTemplate() {
	    return new TestRestTemplate();
	}	
	@Autowired
	private MoviePaginationService movieService;
	
	
	/*public Contro(MoviePaginationService service) {
		this.movieService=service;
	}*/
	public String iterableToString(Iterable<Movie2> movies) {
		List<Movie2> movieslist=new ArrayList();
		movies.forEach(movieslist::add);
		String jsonexpected=JSONArray.toJSONString(movieslist);
		return jsonexpected;
	}	
	
	@Test
	public void testFindall() throws JSONException {
		rest=restTemplate();
		//fail("Not yet implemented");
		String message=this.rest.getForObject("http://localhost:8080/all",String.class);
		//System.out.println(message);
		Iterable<Movie2> movies= movieModel.findAll();
		String jsonexpected=iterableToString(movies);
		//System.out.println(jsonexpected);
		JSONAssert.assertEquals(jsonexpected,message,true);
	}
	
	@Test
	public void testHelo() {
		rest=restTemplate();
		String message=this.rest.getForObject("http://localhost:8080/helo",String.class);
		String expectedMessage="<!DOCTYPE HTML>\r\n" + 
				"\r\n" + 
				"<html lang=\"UTF-8\">\r\n" + 
				"<head>\r\n" + 
				"    <title>Getting Started: Serving Web Content</title>\r\n" + 
				"    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\r\n" + 
				"</head>\r\n" + 
				"<body>\r\n" + 
				"    <p>Helo, World!</p>\r\n" + 
				"</body>\r\n"+ 
				"</html>";
		//System.out.println(message);
		assertNotNull(this.rest.getForObject("http://localhost:8080/helo",String.class));
		assertEquals(expectedMessage.replace("\n", "").replace("\r", ""),message.replace("\n", "").replace("\r", ""));
	}
	
	@Test
	public void testsaveofrequest() throws JSONException {
		rest=restTemplate();
		String movieid="tt0047478";
		//fail("Not yet implemented");
		String message=this.rest.getForObject("http://localhost:8080/one?movieid="+movieid,String.class);
		Iterable<Movie2> movies=movieModel.findBymovieid(movieid);
		String jsonexpected=iterableToString(movies);
		//System.out.println(jsonexpected);
		JSONAssert.assertEquals(jsonexpected,message,true);
	}
	
	@Test
	public void testjsonPage() throws JSONException{
		rest=restTemplate();
		int pageSize=5;
		int requestPage=0;
		//fail("Not yet implemented");
		for(int page=1;page<5;page++) {
			System.out.println("http://localhost:8080/jsonPage/?pageSize="+pageSize+"&page="+page);
			String message=this.rest.getForObject("http://localhost:8080/jsonPage/?pageSize="+pageSize+"&page="+page,String.class);
			System.out.println("MSG:"+message);
			if(page>1) {
				requestPage=page-1;
			}
			Page<Movie2> movies = movieService.findAllPageable(new PageRequest(requestPage, pageSize));
			List<Movie2> listMovies=new ArrayList();
			movies.forEach(listMovies::add);
			String jsonexpected=JSONArray.toJSONString(listMovies);
			jsonexpected="{\"content\":"+jsonexpected;
			//,"totalPages":4,"last":false,"totalElements":16,"size":5,"number":0,"sort":null,"numberOfElements":5,"first":true
			jsonexpected+=",\"totalPages\":"+movies.getTotalPages();
			jsonexpected+=",\"last\":"+movies.isLast();
			jsonexpected+=",\"totalElements\":"+movies.getTotalElements();
			jsonexpected+=",\"size\":"+movies.getSize();
			jsonexpected+=",\"number\":"+movies.getNumber();
			jsonexpected+=",\"sort\":"+movies.getSort();
			jsonexpected+=",\"numberOfElements\":"+movies.getNumberOfElements();
			jsonexpected+=",\"first\":"+movies.isFirst();
			jsonexpected+="}";
			System.out.println("JSONEX:"+jsonexpected);
			System.out.println("");
			JSONAssert.assertEquals(jsonexpected,message,true);
		}
	}

	
	
}
