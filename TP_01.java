import java.io.BufferedReader;
import java.io.FileReader;

/**
 * TP_01
 */
public class TP_01 {

    public static void main(String[] args) throws Exception {
        BufferedReader bfr = new BufferedReader(new FileReader("netflix_titles.csv"));
        while(bfr.ready()){
            System.out.println(bfr.readLine());
        }
        
        bfr.close();
    }
}