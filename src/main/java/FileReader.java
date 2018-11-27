import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileReader {

    private File words;

    public FileReader(File words) {
        this.words = words;
    }

    public List<String> readWords() throws IOException {
        return FileUtils.readLines(this.words, "UTF-8");
    }
}
