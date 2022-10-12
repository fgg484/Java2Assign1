import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MovieAnalyzer {
    String Series_Title;// Name of the movie
    String Released_Year;// Year at which that movie released
    String Certificate;// Certificate earned by that movie
    String Runtime;// Total runtime of the movie
    String Genre;// Genre of the movie
    String IMDB_Rating;// Rating of the movie at IMDB site
    String Overview;// mini story/ summary
    String Meta_score;// Score earned by the movie
    String Director;// Name of the Director
    String Star1, Star2, Star3, Star4;// Name of the Stars
    String Noofvotes;// Total number of votes
    String Gross;// Money earned by that movie

    public MovieAnalyzer(String dataset_path) {
        String[] s = dataset_path.split(",");
    }

}