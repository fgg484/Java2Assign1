import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class MovieAnalyzer {
    String Poster_Link;
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
        int flag = 0, index = 0;
        String now = null;
        for (String ss : s) {
            if (ss.charAt(0) == '\"' || ss.charAt(ss.length() - 1) == '\"') {
                flag++;
                flag %= 2;
            }
            now += ss;
            if (flag == 0) {
                switch (index) {
                    case 0 : this.Poster_Link = now;
                    case 1 : this.Series_Title = now;
                    case 2 : this.Released_Year = now;
                    case 3 : this.Certificate = now;
                    case 4 : this.Runtime = now;
                    case 5 : this.Genre = now;
                    case 6 : this.IMDB_Rating = now;
                    case 7 : this.Overview = now;
                    case 8 : this.Meta_score = now;
                    case 9 : this.Director = now;
                    case 10 : this.Star1 = now;
                    case 11 : this.Star2 = now;
                    case 12 : this.Star3 = now;
                    case 13 : this.Star4 = now;
                    case 14 : this.Noofvotes = now;
                    case 15 : this.Gross = now;
                }
                index++;
                now = null;
            } else {
                now += ",";
            }
        }
    }

    public static void main(String[] args) {
        File file = new File("D:\\study\\G3\\Java2\\Assignment\\A1_Sample\\resources\\imdb_top_500.csv");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String tempString;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                // System.out.println("line " + line + ": " + tempString);
                MovieAnalyzer movieAnalyzer = new MovieAnalyzer(tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}