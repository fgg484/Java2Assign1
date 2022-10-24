import java.io.*;
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

        //V for Vendetta,
        // 2005,
        // A,
        // 132 min,
        // "Action, Drama, Sci-Fi",
        // 8.2,
        // "In a future British tyranny,
        // a shadowy freedom fighter,
        // known only by the alias of ""V"",
        // plots to overthrow it with the help of a young woman.",
        // 62,
        // James McTeigue,
        // Hugo Weaving,
        // Natalie Portman,
        // Rupert Graves,
        // Stephen Rea,
        // 1032749,
        // "70,511,035"

        public Movie(String movie) {
            String[] s = movie.split(",");
            int flag = 0, index = 0;
            String now = "";
            for (String ss : s) {
                int is_null = 0;
                if (ss == null || ss.length() == 0) {
                    now = "NULL";
                    is_null = 1;
                }
                if (is_null == 0) {
                    for (int i = 0; i < ss.length(); i++) {
                        if (ss.charAt(i) == '\"') {
                            flag++;
                        }
                    }
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
                    // System.out.println(now);
                    now = "";
                } else {
                    now += ",";
                }
            }
            if (this.Series_Title.charAt(0) == '\"') {
                this.Series_Title = this.Series_Title.substring(1, this.Series_Title.length() - 1);
            }
            if (this.Overview.charAt(0) == '\"' && this.Overview.charAt(this.getLenOfOverview() - 1) == '\"') {
                this.Overview = this.Overview.substring(1, this.Overview.length() - 1);
            }
//            this.Star1 = this.Star1.replaceAll("\"", "");
//            this.Star2 = this.Star2.replaceAll("\"", "");
//            this.Star3 = this.Star3.replaceAll("\"", "");
//            this.Star4 = this.Star4.replaceAll("\"", "");
            this.Genre = this.Genre.replaceAll("\"", "");
            this.Genre = this.Genre.replaceAll(" ","");
            if (this.Gross.charAt(0) != '\"') {
                this.Gross = "NULL";
            }
            else {
                this.Gross = this.Gross.replaceAll("\"", "");
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

        public float getRating() {
            String[] s = this.IMDB_Rating.split("\\.");
            float ans = s[0].charAt(0) - '0';
            if (s.length != 1) {
                ans += 0.1 * (s[1].charAt(0) - '0');
            }
            return ans;
        }

        public long getGross() {
            if (this.Gross.equals("NULL")) {
                return 0;
            }
            String[] s = this.Gross.split(",");
            int len = s.length;
            long ans = 0;
            for (int i = len - 1; i >= 0; i--) {
                int l = s[i].length();
                long num = 0;
                for (int j = l - 1; j >= 0; j--) {
                    num += (s[i].charAt(j) - '0') * Math.pow(10, l - 1 - j);
//                    System.out.println("num=" + num);
                }
                ans += num * Math.pow(1000, len - 1 - i);
//                System.out.println("ans=" + ans);
            }
            return ans;
        }
    }
    public static List<Movie> movies = new ArrayList<>();
    public MovieAnalyzer(String dataset_path) throws FileNotFoundException, UnsupportedEncodingException {
        File file = new File(dataset_path);
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String tempString;
            int line = 0;
            while ((tempString = reader.readLine()) != null) {
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
        Map<Integer, Integer> ans = new HashMap<>();
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
        for (Movie m : movies) {
            String[] stars = {m.Star1, m.Star2, m.Star3, m.Star4};
            for (int i = 0; i < 3; i++) {
                for (int j = i + 1; j <= 3; j++) {
                    List<String> list = new ArrayList<>();
                    list.add(stars[i]);
                    list.add(stars[j]);
                    list.sort(String::compareTo);
                    Integer cnt = ans.get(list);
                    if (cnt == null) {
                        ans.put(list, 1);
                    }
                    else {
                        ans.put(list, cnt + 1);
                    }
                }
            }
        }
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
        List<String> stars = new ArrayList<>();
        if (by.equals("rating")) {
            Map<String, List<Double>> ans = new HashMap<>();
            Map<String, Double> real_ans = new HashMap<>();
            for (Movie m : movies) {
                String[] s = {m.Star1, m.Star2, m.Star3, m.Star4};
                for (String ss : s) {
                    List<Double> num = ans.get(ss);
                    if (num == null) {
                        num = new ArrayList<>();
                        num.add((double)m.getRating());
                        if (m.getRating() > 0) {
                            num.add(1.0);
                        }
                        else {
                            num.add(0.0);
                        }
                        ans.put(ss, num);
                        real_ans.put(ss, (double)m.getRating());
                    } else {
                        num.set(0, num.get(0) + m.getRating());
                        num.set(1, num.get(1) + 1);
                        ans.put(ss, num);
                        if (num.get(1) != 0) {
                            real_ans.put(ss, (double)(num.get(0) / (double)num.get(1)));
                        }
                        else {
                            real_ans.put(ss, 0.0);
                        }
                    }
                }
            }
            stars = real_ans
                    .entrySet()
                    .stream()
                    .sorted(comparingByKey())
                    .sorted(Collections.reverseOrder(comparingByValue()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }
        else if (by.equals("gross")) {
            Map<String, List<Long>> ans = new HashMap<>();
            Map<String, Long> real_ans = new HashMap<>();
            for (Movie m : movies) {
                String[] s = {m.Star1, m.Star2, m.Star3, m.Star4};
                for (String ss : s) {
                    List<Long> num = ans.get(ss);
                    if (num == null) {
                        num = new ArrayList<>();
                        num.add(m.getGross());
                        if (m.getGross() > 0) {
                            num.add(1L);
                        }
                        else {
                            num.add(0L);
                        }
                        ans.put(ss, num);
                        real_ans.put(ss, m.getGross());
                    } else {
                        num.set(0, num.get(0) + m.getGross());
                        if (m.getGross() == 0)
                            num.set(1, num.get(1));
                        else
                            num.set(1, num.get(1) + 1);
                        ans.put(ss, num);
                        if (num.get(1) != 0) {
                            real_ans.put(ss, num.get(0) / num.get(1));
                        }
                        else {
                            real_ans.put(ss, 0L);
                        }
                    }
                }
            }
            stars = real_ans
                    .entrySet()
                    .stream()
                    .sorted(comparingByKey())
                    .sorted(Collections.reverseOrder(comparingByValue()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }
        return stars.subList(0, top_k);
    }

    public List<String> searchMovies(String genre, float min_rating, int max_runtime) {
        List<String> ans = new ArrayList<>();
        for (Movie m : movies) {
            if (m.getRating() >= min_rating && m.getRuntime() <= max_runtime) {
                String[] genres = m.Genre.split(",");
                for (String s : genres) {
                    s.replaceAll(" ","");
                    if (s.equals(genre)) {
                        ans.add(m.Series_Title);
                        break;
                    }
                }
            }
        }
        return ans
                .stream()
                .sorted(String::compareTo)
                .toList();
    }
//
//    public static void main(String[] args) {
//        MovieAnalyzer movieAnalyzer = new MovieAnalyzer("D:\\study\\G3\\Java2\\Assignment\\A1_Sample\\resources\\imdb_top_500.csv");
////        System.out.println(getCoStarCount().entrySet().stream()
////                .sorted(Collections.reverseOrder(comparingByValue()))
////                .collect(Collectors.toList()));
//            for (Movie m : movies) {
//                String s = "511";
//                if (m.Star1.equals(s) || m.Star2.equals(s) || m.Star3.equals(s) || m.Star4.equals(s)) {
//                    System.out.println(m.Series_Title);
//                    System.out.println(m.Released_Year);
//                    System.out.println(m.Certificate);
//                    System.out.println(m.Runtime);
//                    System.out.println(m.Genre);
//                    System.out.println(m.IMDB_Rating);
//                    System.out.println(m.getRating());
//                    System.out.println(m.Overview);
//                    System.out.println(m.Overview.length());
//                    System.out.println(m.Meta_score);
//                    System.out.println(m.Director);
//                    System.out.println(m.Star1 + "->" + m.Star2 + "->" + m.Star3 + "->" + m.Star4);
//                    System.out.println(m.Noofvotes);
//                    System.out.println(m.Gross);
//                    System.out.println(m.getGross());
//                }
//        }
//    }
}