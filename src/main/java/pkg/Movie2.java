package pkg;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.ngram.EdgeNGramFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;


@AnalyzerDef(name = "customanalyzer", 
/*tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
filters = {
    @TokenFilterDef(factory = LowerCaseFilterFactory.class),
    @TokenFilterDef(factory = EdgeNGramFilterFactory.class,
            params = {
                @Parameter(name = "minGramSize",value = "1"),
                @Parameter(name = "maxGramSize",value = "6")
				})
})*/	
	tokenizer = @TokenizerDef(factory = WhitespaceTokenizerFactory.class), 
	filters = {
		@TokenFilterDef(factory = ASCIIFoldingFilterFactory.class),
		@TokenFilterDef(factory = LowerCaseFilterFactory.class),
        @TokenFilterDef(
                factory = EdgeNGramFilterFactory.class, // Generate prefix tokens
                params = {
                        @Parameter(name = "minGramSize", value = "1"),
                        @Parameter(name = "maxGramSize", value = "3")
                }
        )

	}
)

@Entity
@Table(name="movie")
@Indexed
public class Movie2 {
    @Id
    @Column(name = "movieid", nullable = false, updatable = false)
    //Field is implicit by ID annotation
    private String movieid;
    
    @Field(name="title",index=Index.YES,analyze=Analyze.YES,store=Store.YES)
    @Analyzer(definition="customanalyzer")
	private String title;
    
    @Field(index = Index.NO, analyze=Analyze.NO, store = Store.NO)
	private String release_year;
    
    @Field(index = Index.NO, analyze=Analyze.NO, store = Store.NO)
	private String rating;
    
    @Field(index = Index.NO, analyze=Analyze.NO, store = Store.NO)
	private String image;
	
	public String getMovieid() {
		return movieid;
	}

	public void setMovieid(String movieid) {
		this.movieid = movieid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRelease_year() {
		return release_year;
	}

	public void setRelease_year(String release_year) {
		this.release_year = release_year;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
}
