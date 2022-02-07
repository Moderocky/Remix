package mx.kenzie.remix.test;

import mx.kenzie.remix.compiler.CompileContext;
import mx.kenzie.remix.lang.function.FunctionName;
import mx.kenzie.remix.lang.function.FunctionParameter;
import mx.kenzie.remix.lang.function.FunctionParameterName;
import mx.kenzie.remix.lang.function.KeywordFunction;
import mx.kenzie.remix.lang.type.KeywordExtends;
import mx.kenzie.remix.lang.type.KeywordType;
import mx.kenzie.remix.lang.type.TypeExtends;
import mx.kenzie.remix.lang.type.TypeName;
import mx.kenzie.remix.parser.RemixParser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ParsingTest {
    
    @Test
    public void test() throws IOException {
        final CompileContext context = new CompileContext(
            new KeywordType(),
            new TypeName(),
            new KeywordExtends(),
            new TypeExtends(),
            new KeywordFunction(),
            new FunctionName(),
            new FunctionParameter(),
            new FunctionParameterName()
        );
        final String test = """
            type house is home:
                func MyFunc string name:
                ;
            ;
            """;
        final InputStream stream = new ByteArrayInputStream(test.getBytes(StandardCharsets.UTF_8));
        final RemixParser parser = new RemixParser(stream, context);
        parser.parse();
    }
    
}
