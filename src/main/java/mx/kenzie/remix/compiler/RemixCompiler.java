package mx.kenzie.remix.compiler;

import mx.kenzie.remix.builder.TypeBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class RemixCompiler {
    
    protected final CompileContext context;
    protected final RuntimeClassLoader loader = new RuntimeClassLoader();
    
    public RemixCompiler(CompileContext context) {
        this.context = context;
    }
    
    public void compile(OutputStream stream) throws IOException {
        final TypeBuilder builder = context.internalBuilders.remove(0);
        final byte[] bytes = builder.writer().toByteArray();
        stream.write(bytes);
    }
    
    public void compileAll(File directory) {
        if (!directory.exists()) directory.mkdirs();
        assert directory.isDirectory();
        for (final TypeBuilder builder : context.internalBuilders) {
            final File file = new File(directory, builder.getType().getTypeName() + ".class");
            try (final OutputStream stream = new FileOutputStream(file)) {
                final byte[] bytes = builder.writer().toByteArray();
                stream.write(bytes);
            } catch (IOException exception) {
                throw new Error("Unable to write output of '" + builder.getType().name() + "'", exception);
            }
        }
    }
    
    public Class<?>[] loadAll() {
        final List<Class<?>> classes = new ArrayList<>();
        for (final TypeBuilder builder : context.internalBuilders) {
            final Class<?> loaded = loader.load(builder.getType(), builder.writer().toByteArray());
            classes.add(loaded);
        }
        return classes.toArray(new Class[0]);
    }
    
}
