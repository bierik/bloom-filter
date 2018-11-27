import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

public class BloomFilter {

    private int n;
    private double p;
    private int k;
    private int m;
    private boolean[] filter;
    private Function[] hashFunctions;
    private static final double log2 = Math.log(2);

    public BloomFilter(int n, double p) {
        if (n <= 0) {
            throw new IllegalArgumentException("Number of inserted elements must be greater than 0");
        }
        if (p > 1 || p < 0) {
            throw new IllegalArgumentException("Expected false positive probability must be between 0 and 1 (including)");
        }
        this.n = n;
        this.p = p;

        this.k = this.calcNumberOfHashFunctions();
        this.m = this.calcSizeOfFilter();

        hashFunctions = new Function[this.k];
        this.filter = new boolean[this.m];

        for(int i = 0; i < this.k; i++) {
            hashFunctions[i] = new HashFunction(i);
        }
    }

    private int calcNumberOfHashFunctions() {
        return (int) Math.ceil(((Math.log(this.p)) / log2) * (-1));
    }

    private int calcSizeOfFilter() {
        return (int) Math.ceil(((n * Math.log(this.p)) / Math.pow(log2, 2)) * (-1));
    }

    public void insert(String word) {
        int[] hashes = new int[this.k];
        for(int i = 0; i < k; i++) {
            int index = Math.abs((int) this.hashFunctions[i].apply(word)) % this.m;
            this.filter[index] = true;
        }
    }

    private boolean query(String word) {
        int[] hashes = new int[this.k];
        for(int i = 0; i < k; i++) {
            int index = Math.abs((int) this.hashFunctions[i].apply(word)) % this.m;
            if (!this.filter[index]) {
                return false;
            }
        }
        return true;
    }

    public static void test(double p, File trainFile, File testFile) throws IOException {
        FileReader trainFileReader = new FileReader(trainFile);
        List<String> trainWords = trainFileReader.readWords();
        int n = trainWords.size();
        BloomFilter bloomFilter = new BloomFilter(n, p);
        trainWords.stream().forEach(w -> bloomFilter.insert(w));

        FileReader testFileReader = new FileReader(testFile);
        List<String> testWords = testFileReader.readWords();
        int testWordSize = testWords.size();
        int notFound = (int) testWords.stream().map(f -> bloomFilter.query(f)).filter(q -> q == false).count();

        System.out.println("Konfiguration des Bloom-Filters:");
        System.out.println(String.format("Anzahl eingefügte Wörter: %d", n));
        System.out.println(String.format("Fehlerwahrscheinlichkeit: %1.4f", p));
        System.out.println(String.format("Anzahl Hashfunktionen: %d", bloomFilter.k));
        System.out.println(String.format("Grösse des Bitarrays: %d", bloomFilter.m));
        System.out.println("------------------------------------");
        System.out.println("Ergebnisse nach Testdruchlauf:");
        System.out.println(String.format("Anzahl getestete Wörter: %d", testWordSize));
        System.out.println(String.format("Anazhl false positives: %d", testWordSize - notFound));
        System.out.println(String.format("Effektive Fehlerwahrscheinlichkeit: %1.4f", ((testWordSize - notFound) / (double) testWordSize)));
    }
}
