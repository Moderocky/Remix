package mx.kenzie.remix.test;

import mx.kenzie.remix.compiler.CompileContext;
import mx.kenzie.remix.compiler.RemixCompiler;
import mx.kenzie.remix.parser.RemixParser;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;

public class JarTest extends RemixTest {
    
    private static RemixCompiler compiler;
    
    @BeforeClass
    public static void start() throws Throwable {
        final CompileContext context = context();
        final InputStream stream = resource("field.rmx");
        final RemixParser parser = new RemixParser(stream, context);
        parser.parse();
        compiler = new RemixCompiler(context);
    }
    
    @Test
    public void testMain() throws Throwable {
        assert compiler != null : "Compiler was not created.";
        final File file = new File("target/generated-rmx-results/Test.jar");
        if (file.exists()) file.delete();
        assert !file.exists();
        compiler.compileKit(new File("target/generated-rmx-classes/"));
        compiler.compileAll(new File("target/generated-rmx-classes/"));
        compiler.writeJar(file);
        assert file.exists();
    }
}
