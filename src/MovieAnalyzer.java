import javax.lang.model.element.Element;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByKey;
import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

public class MovieAnalyzer {
    public class Movie {
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

        //"https://m.media-amazon.com/images/M/MV5BOTc2ZTlmYmItMDBhYS00YmMzLWI4ZjAtMTI5YTBjOTFiMGEwXkEyXkFqcGdeQXVyODE5NzE3OTE@._V1_UY98_CR0,0,67,98_AL_.jpg",
        // Soorarai Pottru,
        // 2020,
        // U,
        // 153 min,
        // Drama,
        // 8.6,
        // "Nedumaaran Rajangam ""Maara"" sets out to make the common man fly and in the process takes on the world's most capital intensive industry and several enemies who stand in his way.",
        // ,
        // Sudha Kongara,
        // Suriya,
        // Madhavan,
        // Paresh Rawal,
        // Aparna Balamurali,
        // 54995,
        //

        public Movie(String movie) {
            String[] s = movie.split(",");
            int flag = 0, index = 0;
            String now = "";
            for (String ss : s) {
                if (ss == null || ss.length() == 0) {
                    now = "NULL";
                }
                else if (ss.charAt(0) == '\"' || ss.charAt(ss.length() - 1) == '\"') {
                    if (!(ss.charAt(0) == '\"' && ss.charAt(ss.length() - 1) == '\"')) {
                        flag++;
                        flag %= 2;
                    }
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
                    // System.out.println(now);
                    now = "";
                } else {
                    now += ",";
                }
            }
            if (this.Series_Title.charAt(0) == '\"') {
                this.Series_Title = this.Series_Title.substring(1, this.Series_Title.length() - 1);
            }
            if (this.Overview.charAt(0) == '\"') {
                this.Overview = this.Overview.substring(1, this.Overview.length() - 1);
            }
            if (this.Gross.charAt(0) != '\"') {
                this.Gross = "NULL";
            }
        }

        public int getRuntime() {
            String[] time = this.Runtime.split(" ");
            int len = time[0].length(), ans = 0;
            int l = len - 1;
            while(l >= 0) {
                ans += (time[0].charAt(l) - '0') * Math.pow(10, len - 1 - l);
                l--;
            }
            return ans;
        }

        public String getSeries_Title() {
            return this.Series_Title;
        }

        public int getLenOfOverview() {
            return this.Overview.length();
        }
    }
    public static List<Movie> movies = new ArrayList<>();
    public MovieAnalyzer(String dataset_path) {
        File file = new File(dataset_path);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String tempString;
            int line = 0;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                // System.out.println("line " + line + ": " + tempString);
                // System.out.println();
                Movie movie = new Movie(tempString);
                if (line > 0)
                    movies.add(movie);
                line++;
            }
            // System.out.println(getMovieCountByYear());
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Map<Integer, Integer> getMovieCountByYear() {
//        int[] tong = new int[5000];
//        for (Movie m : movies) {
//            int year = getYear(m);
//            tong[year]++;
//        }
        Map<Integer, Integer> ans = new HashMap<>();
//        for (int i = 1850; i <= 2025; i++) {
//            if (tong[i] != 0) {
//                ans.put(i, tong[i]);
//                // System.out.println(i + " " + tong[i]);
//            }
//        }
        for (Movie m : movies) {
            int year = getYear(m);
            Integer cnt = ans.get(year);
            if (cnt == null) {
                ans.put(year, 1);
            }
            else {
                ans.put(year, cnt + 1);
            }
        }
        //System.out.println(ans);
//        转化为list的方式
//        List<Map.Entry<Integer, Integer>> list = new ArrayList<Map.Entry<Integer, Integer>>(ans.entrySet());
//        list.sort(new Comparator<Map.Entry<Integer, Integer>>() {
//          public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
//              return o2.getValue().compareTo(o1.getValue());
//          }
//        });

        return ans
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(comparingByKey()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    private int getYear(Movie movie) {
        String s = movie.Released_Year;
        int l = 3, ans = 0;
        while (l >= 0) {
            int pos = s.charAt(l) - '0';
            ans += pos * Math.pow(10, 3 - l);
            l--;
        }
        return ans;
    }
    public Map<String, Integer> getMovieCountByGenre() {
        Map<String, Integer> ans = new HashMap<>();
        for (Movie m : movies) {
            String[] genres = m.Genre.split(",");
            for (String genre : genres) {
                genre = genre.replaceAll(" ", "");
                genre = genre.replaceAll("\"","");
                Integer cnt = ans.get(genre);
                if (cnt == null) {
                    ans.put(genre, 1);
                } else {
                    ans.put(genre, cnt + 1);
                }
            }
        }
        return ans
                .entrySet()
                .stream()
                .sorted(comparingByKey())
                .sorted(Collections.reverseOrder(comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public Map<List<String>, Integer> getCoStarCount() {
        Map<List<String>, Integer> ans = new HashMap<>();

        return ans;
    }

    public List<String> getTopMovies(int top_k, String by) {
        List<String> ans = new ArrayList<>();
        if (by.equals("runtime")) {
            List<Movie> ans_runtime = movies
                    .stream()
                    .sorted(Comparator.comparing(Movie::getRuntime, Comparator.reverseOrder()).thenComparing(Movie::getSeries_Title))
                    .toList();
            int cnt = 0;
            for (Movie m : ans_runtime) {
                if (cnt == top_k)
                    break;
                ans.add(m.Series_Title);
                cnt++;
            }
        }
        else if (by.equals("overview")) {
            List<Movie> ans_overview = movies
                    .stream()
                    .sorted(Comparator.comparing(Movie::getLenOfOverview, Comparator.reverseOrder()).thenComparing(Movie::getSeries_Title))
                    .toList();
            int cnt = 0;
            for (Movie m : ans_overview) {
                if (cnt == top_k)
                    break;
                ans.add(m.Series_Title);
                cnt++;
            }
        }
        return ans;
    }

    public List<String> getTopStars(int top_k, String by) {
        List<String> ans = new ArrayList<>();
        return ans;
    }

    public List<String> searchMovies(String genre, float min_rating, int max_runtime) {
        List<String> ans = new ArrayList<>();
        return ans;
    }

    public static void main(String[] args) {
        MovieAnalyzer movieAnalyzer = new MovieAnalyzer("D:\\study\\G3\\Java2\\Assignment\\A1_Sample\\resources\\imdb_top_500.csv");
        for (Movie m : movies) {
            if (m.Series_Title.equals("Metropolis") || m.Series_Title.equals("Indiana Jones and the Last Crusade") || m.Series_Title.equals("3 Idiots") || m.Series_Title.equals("Casino Royale")) {
                System.out.println(m.Series_Title);
//                System.out.println(m.Released_Year);
//                System.out.println(m.Certificate);
//                System.out.println(m.Runtime);
//                System.out.println(m.Genre);
//                System.out.println(m.IMDB_Rating);
                System.out.println(m.Overview);
                System.out.println(m.Overview.length());
//                System.out.println(m.Meta_score);
//                System.out.println(m.Director);
//                System.out.println(m.Star1 + "->" + m.Star2 + "->" + m.Star3 + "->" + m.Star4);
//                System.out.println(m.Noofvotes);
//                System.out.println(m.Gross);
            }
        }
        // System.out.println(getTopMovies(10, "runtime"));
    }
}