package mx.kenzie.remix.test;

import mx.kenzie.remix.compiler.CompileContext;
import mx.kenzie.remix.compiler.RemixCompiler;
import mx.kenzie.remix.parser.RemixParser;
import org.junit.BeforeClass;
import org.junit.Test;
import rmx.system;

import java.io.InputStream;
import java.lang.reflect.Method;

public class OperatorTest extends RemixTest {
    
    private static Class<?> loaded;
    
    @BeforeClass
    public static void start() throws Throwable {
        final CompileContext context = context();
        final InputStream stream = resource("operator.rmx");
        final RemixParser parser = new RemixParser(stream, context);
        parser.parse();
        final RemixCompiler compiler = new RemixCompiler(context);
//        compiler.compileAll(new File("blob/"));
        final Class<?>[] classes = compiler.loadAll();
        loaded = classes[1];
    }
    
    @Test
    public void testMain() throws Throwable {
        final Method method = loaded.getMethod("Main");
        method.invoke(system.system().Allocate(loaded));
        
    }
}
