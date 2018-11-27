import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class FileReader {

    private File words;

    public FileReader(File words) {
        this.words = words;
    }

    /**
     * Reads a word for every lines and converts it into a list of strings.
     * @return List of words in the file for every line.
     * @throws IOException
     */
    public List<String> readWords() throws IOException {
        return FileUtils.readLines(this.words, Charset.defaultCharset().toString());
    }
}
