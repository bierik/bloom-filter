import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

    public static void main(String[] args) {
        try {
            File trainFile = new File(Main.class.getResource("words.txt").toURI());
            File testFile = new File(Main.class.getResource("test.txt").toURI());
            BloomFilter.test(0.01, trainFile, testFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
