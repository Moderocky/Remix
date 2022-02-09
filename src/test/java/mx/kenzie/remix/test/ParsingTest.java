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
import mx.kenzie.remix.meta.TypeStub;
import mx.kenzie.remix.parser.RemixParser;
import org.junit.Test;
import org.objectweb.asm.ClassWriter;
import rmx.string;
import rmx.system;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

public class ParsingTest {
    
    @Test
    public void test() throws Throwable {
        final CompileContext context = new CompileContext(
            new TypeStub[]{
                TypeStub.of(rmx.object.class),
                TypeStub.of(rmx.system.class),
                TypeStub.of(rmx.string.class),
                TypeStub.of(rmx.number.class),
                TypeStub.of(rmx.integer.class),
                TypeStub.of(rmx.decimal.class),
                TypeStub.of(rmx.type.class),
                TypeStub.of(rmx.pointer.class),
            },
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
            
            new KeywordDuplicate(),
            new KeywordPop(),
            new KeywordSwap(),
            new KeywordReturn(),
            new KeywordAllocate(),
            new TypeAllocate(),
            new KeywordNew(),
            new TypeNew(),
            new KeywordCast(),
            new TypeCast(),
            
            new ConstantThis(),
            new ConstantSystem(),
            
            new FunctionGet(),
            new FunctionCall(),
            
            new LiteralString(),
            new LiteralInteger(),
            new LiteralDecimal()
        );
        final String test = """
            type house is object:
                
                object thing
                trans object blob
                
                func TestFunc:
                    return: "hello";
                ;
                
                func MyFunc string name:
                    system.Print: this.TestFunc;
                ;
                
            ;
            """;
        final InputStream stream = new ByteArrayInputStream(test.getBytes(StandardCharsets.UTF_8));
        final RemixParser parser = new RemixParser(stream, context);
        parser.parse();
        final ClassWriter writer = context.currentType().writer();
        new FileOutputStream("test.class").write(writer.toByteArray()); // todo
        final Class<?> loaded = new ClassLoader() {
            public Class<?> load(byte[] bytes) {
                return this.defineClass("rmx.house", bytes, 0, bytes.length);
            }
        }.load(writer.toByteArray());
        final Method method = loaded.getMethod("MyFunc", rmx.string.class);
        method.invoke(system.system().Allocate(loaded), new string("hello"));
    }
    
}
