import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

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

    public static List<MovieAnalyzer> movieAnalyzerList = new ArrayList<>();
    public MovieAnalyzer(String dataset_path) {
        String[] s = dataset_path.split(",");
        int flag = 0, index = 0;
        String now = "";
        if (s.length < 16) {
            this.Gross = "NULL";
        }
        for (String ss : s) {
            if (ss == null || ss.length() == 0) {
                now = "NULL";
            }
            else if (ss.charAt(0) == '\"' || ss.charAt(ss.length() - 1) == '\"') {
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
                // System.out.println(now);
                now = "";
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
                // System.out.println();
                MovieAnalyzer movieAnalyzer = new MovieAnalyzer(tempString);
                movieAnalyzerList.add(movieAnalyzer);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, Integer> getMovieCountByYear() {
        int[] tong = new int[50000];
        for (MovieAnalyzer m : movieAnalyzerList) {
            int year = m.getYear();
            tong[year]++;
        }
        Map<Integer, Integer> ans = new HashMap<Integer, Integer>();
        for (int i = 1900; i <= 2025; i++) {
            if (tong[i] != 0) {
                ans.put(i, tong[i]);
                // System.out.println(i + " " + tong[i]);
            }
        }
//        转化为list的方式
//        List<Map.Entry<Integer, Integer>> list = new ArrayList<Map.Entry<Integer, Integer>>(ans.entrySet());
//        list.sort(new Comparator<Map.Entry<Integer, Integer>>() {
//          public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
//              return o2.getValue().compareTo(o1.getValue());
//          }
//        });
        ans = ans
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
        return ans;
    }

    private int getYear() {
        String s = this.Released_Year;
        int l = 3, ans = 0;
        while (l >= 0) {
            int pos = s.charAt(l) - '0';
            ans += pos * Math.pow(10, 3 - l);
            l--;
        }
        return ans;
    }
}