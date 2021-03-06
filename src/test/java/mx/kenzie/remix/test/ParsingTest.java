package mx.kenzie.remix.test;

import mx.kenzie.remix.compiler.CompileContext;
import mx.kenzie.remix.compiler.RemixCompiler;
import mx.kenzie.remix.parser.RemixParser;
import org.junit.BeforeClass;
import org.junit.Test;
import rmx.string;
import rmx.system;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;

public class ParsingTest extends RemixTest {
    
    private static Class<?> loaded;
    
    @BeforeClass
    public static void start() throws Throwable {
        final CompileContext context = context();
        final InputStream stream = resource("structure.rmx");
        final RemixParser parser = new RemixParser(stream, context);
        parser.parse();
        final RemixCompiler compiler = new RemixCompiler(context);
        compiler.compileAll(new File("target/generated-rmx-classes"));
        final Class<?>[] classes = compiler.loadAll();
        for (final Class<?> type : classes) if (type.getSimpleName().equals("house")) loaded = type;
    }
    
    @Test
    public void testMyFunc() throws Throwable {
        final Method method = loaded.getMethod("MyFunc", rmx.string.class);
        final rmx.string string = (string) method.invoke(system.system().Allocate(loaded), new string("hello"));
        assert string.value().equals("hello");
    }
    
    @Test
    public void testTest() throws Throwable {
        final Method method = loaded.getMethod("Test");
        final rmx.string string = (string) method.invoke(system.system().Allocate(loaded));
        assert string.value().equals("hello there");
    }
    
    @Test
    public void testTestFunc() throws Throwable {
        final Method method = loaded.getMethod("TestFunc");
        final rmx.string string = (string) method.invoke(system.system().Allocate(loaded));
        assert string.value().equals("hello");
    }
    
}
