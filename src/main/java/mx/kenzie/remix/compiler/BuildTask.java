package mx.kenzie.remix.compiler;

import mx.kenzie.remix.builder.TypeBuilder;
import mx.kenzie.remix.meta.TypeStub;
import mx.kenzie.remix.parser.RemixParser;
import mx.kenzie.remix.parser.RemixScrapeParser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class BuildTask {
    
    protected final Set<TypeStub> types = new HashSet<>();
    protected final List<TypeBuilder> builders = new ArrayList<>();
    protected final File[] files;
    protected final Supplier<CompileContext> supplier;
    
    public BuildTask(Supplier<CompileContext> supplier, File... files) {
        this.files = files;
        this.supplier = supplier;
    }
    
    public void search() throws IOException {
        for (final File file : files) {
            if (!file.exists()) continue;
            try (final InputStream stream = new FileInputStream(file)) {
                final CompileContext context = supplier.get();
                context.types.addAll(types);
                final RemixParser parser = new RemixScrapeParser(stream, context);
                parser.parse();
                this.types.addAll(context.types);
            }
        }
    }
    
    public void parse() throws IOException {
        for (final File file : files) {
            if (!file.exists()) continue;
            try (final InputStream stream = new FileInputStream(file)) {
                final CompileContext context = supplier.get();
                context.types.addAll(types);
                final RemixParser parser = new RemixParser(stream, context);
                parser.parse();
                this.builders.addAll(context.internalBuilders);
            }
        }
    }
    
    public void parseCompile(File folder) throws IOException {
        for (final File file : files) {
            if (!file.exists()) continue;
            final TypeBuilder[] builders;
            try (final InputStream stream = new FileInputStream(file)) {
                final CompileContext context = supplier.get();
                context.types.addAll(types);
                final RemixParser parser = new RemixParser(stream, context);
                parser.parse();
                builders = context.internalBuilders.toArray(new TypeBuilder[0]);
            }
            for (final TypeBuilder builder : builders) {
                final File output = new File(folder, builder.getType().name() + ".class");
                if (!output.exists()) output.createNewFile();
                try (final OutputStream stream = new FileOutputStream(output)) {
                    stream.write(builder.writer().toByteArray());
                }
            }
        }
    }
    
    public void compile(File folder) throws IOException {
        for (final TypeBuilder builder : builders) {
            final File file = new File(folder, builder.getType().name() + ".class");
            if (!file.exists()) file.createNewFile();
            try (final OutputStream stream = new FileOutputStream(file)) {
                stream.write(builder.writer().toByteArray());
            }
        }
    }
    
}
