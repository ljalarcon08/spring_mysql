package pkg;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.lucene.search.Query;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class HibernateSearchService {
	private final Logger logger=(Logger)LoggerFactory.getLogger(HibernateSearchService.class);
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public List<Movie2> titleIdSearch(String titleIdToSearch){
		FullTextEntityManager fullTextEntityManager=Search.getFullTextEntityManager(entityManager);
		QueryBuilder qb=fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Movie2.class).get();

		System.out.println(titleIdToSearch+"*");
		Query luceneQuery=qb.keyword().onFields("title").matching(titleIdToSearch+"*").createQuery();
		
		
		javax.persistence.Query jpaQuery=fullTextEntityManager.createFullTextQuery(luceneQuery,Movie2.class);
		
		List<Movie2> movies=null;
		try {
			movies=jpaQuery.getResultList();
			System.out.println(""+movies.size());
		}catch(NoResultException e) {
			logger.warn("No results");
			System.out.println("No results"+movies.size());
		}
		return movies;
	}
	
	@Transactional
	public Page<Movie2> titleIdSearchByPage(String titleIdToSearch,int page,int pageSize){
		FullTextEntityManager fullTextEntityManager=Search.getFullTextEntityManager(entityManager);
		QueryBuilder qb=fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Movie2.class).get();

		System.out.println(titleIdToSearch+"*");
		Query luceneQuery=qb.keyword().onFields("title").matching(titleIdToSearch+"*").createQuery();
		
		int totalSize=fullTextEntityManager
				.createFullTextQuery(luceneQuery,Movie2.class).getResultSize();
		
		javax.persistence.Query jpaQuery=fullTextEntityManager
					.createFullTextQuery(luceneQuery,Movie2.class)
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		int limit=pageSize*page+pageSize;
		int offset=pageSize*page;
		
		jpaQuery.setMaxResults(limit);
		
		jpaQuery.setFirstResult(offset);
		
		List<Movie2> movieList=null;
		Page<Movie2> movies=null;
		try {
			movieList=jpaQuery.getResultList();

			Pageable pageable=new PageRequest(page,pageSize);

			movies=new PageImpl<Movie2>(movieList,pageable,totalSize);
		}catch(NoResultException e) {
			logger.warn("No results");
		}
		return movies;
	}
	
	
}
