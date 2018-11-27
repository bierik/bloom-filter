import com.google.common.hash.Hashing;

import java.nio.charset.Charset;
import java.util.function.Function;

public class HashFunction implements Function<String, Integer> {
    private int seed;

    public HashFunction(int seed) {
        this.seed = seed;
    }

    @Override
    public Integer apply(String s) {
        return Hashing.murmur3_128(this.seed).hashString(s, Charset.defaultCharset()).asInt();
    }
}
