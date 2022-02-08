package mx.kenzie.remix.test;

import mx.kenzie.remix.compiler.CompileContext;
import mx.kenzie.remix.lang.code.*;
import mx.kenzie.remix.lang.field.KeywordTransient;
import mx.kenzie.remix.lang.field.NameField;
import mx.kenzie.remix.lang.field.TypeField;
import mx.kenzie.remix.lang.function.FunctionName;
import mx.kenzie.remix.lang.function.FunctionParameter;
import mx.kenzie.remix.lang.function.FunctionParameterName;
import mx.kenzie.remix.lang.function.KeywordFunction;
import mx.kenzie.remix.lang.literal.LiteralDecimal;
import mx.kenzie.remix.lang.literal.LiteralInteger;
import mx.kenzie.remix.lang.literal.LiteralString;
import mx.kenzie.remix.lang.type.KeywordExtends;
import mx.kenzie.remix.lang.type.KeywordType;
import mx.kenzie.remix.lang.type.TypeExtends;
import mx.kenzie.remix.lang.type.TypeName;
import mx.kenzie.remix.parser.RemixParser;
import org.junit.Test;
import org.objectweb.asm.ClassWriter;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
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
            new FunctionParameterName(),
            
            new KeywordTransient(),
            new TypeField(),
            new NameField(),
            
            new KeywordReturn(),
            new KeywordPrint(), // test only
            new KeywordAllocate(),
            new TypeAllocate(),
            new KeywordNew(),
            new TypeNew(),
            new KeywordCast(),
            new TypeCast(),
            
            new LiteralString(),
            new LiteralInteger(),
            new LiteralDecimal()
        );
        final String test = """
            type house is object:
                
                object thing
                trans object blob
                
                func MyFunc string name:
                    print: "hello";
                    print: 10.5;
                    return: 10;
                ;
            ;
            """;
        final InputStream stream = new ByteArrayInputStream(test.getBytes(StandardCharsets.UTF_8));
        final RemixParser parser = new RemixParser(stream, context);
        parser.parse();
        final ClassWriter writer = context.currentType().writer();
        new FileOutputStream("test.class").write(writer.toByteArray()); // todo
    }
    
}
