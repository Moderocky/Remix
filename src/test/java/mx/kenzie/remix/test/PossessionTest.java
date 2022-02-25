package mx.kenzie.remix.test;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class PossessionTest {
    
    @Test
    public void test() {
        assert longHash("hello there!") == 2728214731356992843L;
        assert longHash("myMethod") == 3108564162989L;
        assert Long.toHexString(longHash("hello there!")).equals("25dc90d7ed85754b");
        assert Long.toHexString(longHash("myMethod")).equals("2d3c4dcd9ad");
        assert Long.toHexString(longHash("(Ljava/lang/Object;)V")).equals("2bcac5655d527811");
    }
    
    private static long longHash(String string) {
        final byte[] bytes = string.getBytes(StandardCharsets.US_ASCII);
        long hash = 0;
        for (final byte b : bytes) hash = 31 * hash + (b & 0xff);
        return hash;
    }
    
}
