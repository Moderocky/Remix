package mx.kenzie.remix.test;

import mx.kenzie.remix.app.RemixApp;
import mx.kenzie.remix.compiler.CompileContext;

import java.io.InputStream;

public abstract class RemixTest {
    
    static CompileContext context() {
        return RemixApp.defaultContext();
    }
    
    static InputStream resource(String name) {
        return RemixTest.class.getClassLoader().getResourceAsStream(name);
    }
    
}
