import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;

public class HashingFunction {
    private int seed;
    private HashFunction hashFunction;

    public HashingFunction(int seed) {
        this.seed = seed;
        this.hashFunction = Hashing.murmur3_128(this.seed);
    }

    /**
     * Calculates an index using the murmur3_128 hashing function.
     * @param word Word to calculate the hash from.
     * @param m Size of bitarray of the bloom filter.
     * @return Number between zero (including) and size of bitarray (excluding)
     */
    public int calcIndex(String word, int m) {
        return Math.abs(this.hashFunction.hashString(word, Charset.defaultCharset()).asInt()) % m;
    }
}
