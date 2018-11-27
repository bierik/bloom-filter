import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class BloomFilter {

    // Number of words in filter
    private int n;

    // Expected false positive probability
    private double p;

    // Number of hashing functions
    private int k;

    // Size of bitarray
    private int m;

    // List of hashing functions
    private HashingFunction[] hashingFunctions;

    // Bitarray
    private boolean[] filter;

    private static final double log2 = Math.log(2);

    /**
     * Constructs BloomFilter instance with given number of elements and expected probability
     * @param n Number of elements
     * @param p Expected probability
     */
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

        this.filter = new boolean[this.m];
        this.hashingFunctions = new HashingFunction[this.k];

        // Fill hashing functions, each with different seed
        for(int i = 0; i < this.k; i++) {
            hashingFunctions[i] = new HashingFunction(i);
        }
    }

    /**
     * Calculates the optimal amount of hashing functions
     * See https://en.wikipedia.org/wiki/Bloom_filter#Optimal_number_of_hash_functions
     * @return Optimal amount of hashing functions
     */
    private int calcNumberOfHashFunctions() {
        return (int) Math.ceil(((Math.log(this.p)) / log2) * (-1));
    }

    /**
     * Calculates the optimal size for bitarray
     * See https://en.wikipedia.org/wiki/Bloom_filter#Optimal_number_of_hash_functions
     * @return Optimal size of bitarray
     */
    private int calcSizeOfFilter() {
        return (int) Math.ceil(((n * Math.log(this.p)) / Math.pow(log2, 2)) * (-1));
    }

    /**
     * Calculates hashes for word using all hashing functions
     * @param word Word to insert in the filter
     */
    public void insert(String word) {
        Arrays.stream(this.hashingFunctions)
            .map(h -> h.calcIndex(word, this.m))
            .forEach(i -> this.filter[i] = true);
    }

    /**
     * Queries the filter to determine if the word is maybe present of not present
     * @param word Word to query for the filter
     * @return true if word id maybe present, false if word is not present
     */
    private boolean query(String word) {
        return Arrays.stream(this.hashingFunctions)
            .map(h -> h.calcIndex(word, this.m))
            .anyMatch(i -> !this.filter[i]);
    }

    /**
     * Runs a probability test for a given probability, training file and testing file.
     * Each word must sit on a separate line.
     * Prints a summary of the test result.
     * @param p Expected probability
     * @param trainFile File containing training data
     * @param testFile File containing testing data
     * @throws IOException
     */
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
        System.out.println(String.format("Anazhl false positives: %d", notFound));
        System.out.println(String.format("Effektive Fehlerwahrscheinlichkeit: %1.4f", notFound / (double) testWordSize));
    }
}
