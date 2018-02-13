package pkg;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Ratings {
	private Collection<Rating> rating;
	
	public Ratings() {
	}

	public Collection<Rating> getRating() {
		return rating;
	}

	public void setRating(Collection<Rating> rating) {
		this.rating = rating;
	}

	@Override
	public String toString() {
		return "Ratings [rating=" + rating + "]";
	}
	
}
