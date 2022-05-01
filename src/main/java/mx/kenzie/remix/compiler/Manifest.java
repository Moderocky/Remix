package mx.kenzie.remix.compiler;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public record Manifest() {
    
    public byte[] data() {
        return this.toString().getBytes(StandardCharsets.UTF_8);
    }
    
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : this.values().entrySet())
            builder.append(entry.getKey()).append(": ").append(entry.getValue()).append('\n');
        return builder.toString();
    }
    
    public Map<String, String> values() {
        Map<String, String> map = new HashMap<>();
        map.put("Manifest-Version", "1.0");
        map.put("Archiver-Version", "Zip");
        map.put("Created-By", "Remix Compiler");
        map.put("Main-Class", "rmx.system");
        return map;
    }
}
