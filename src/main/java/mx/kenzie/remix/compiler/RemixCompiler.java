package mx.kenzie.remix.compiler;

import mx.kenzie.jupiter.stream.InternalAccess;
import mx.kenzie.remix.builder.TypeBuilder;
import mx.kenzie.remix.meta.FunctionStub;
import mx.kenzie.remix.meta.TypeStub;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import rmx.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

public class RemixCompiler {
    
    protected final CompileContext context;
    protected final RuntimeClassLoader loader = new RuntimeClassLoader();
    
    public RemixCompiler(CompileContext context) {
        this.context = context;
        this.context.finish();
    }
    
    public void writeJar(File file) throws IOException {
        file.getParentFile().mkdirs();
        if (!file.exists()) file.createNewFile();
        final ZipOutputStream stream = new ZipOutputStream(new FileOutputStream(file));
        stream.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
        stream.write(new Manifest().data());
        stream.closeEntry();
        this.compileKit(stream);
        this.compileAll(stream);
        stream.close();
    }
    
    public void compileKit(ZipOutputStream stream) throws IOException {
        if (context.internalBuilders.isEmpty()) return;
        final Class<?>[] library = new Class[]{array.class, decimal.class, error.class, integer.class, number.class, object.class, object.object_0.class, pointer.class, string.class, system.class, type.class, InternalAccess.class, InternalAccess.AccessUnsafe.class};
        for (final Class<?> real : library) {
            final TypeStub type = TypeStub.of(real);
            for (final FunctionStub method : type.methods()) {
                final TypeStub stub = method.getFunctionalInterface();
                final ZipEntry entry = new ZipEntry(stub.internal() + ".class");
                try {
                    stream.putNextEntry(entry);
                } catch (ZipException ex) {
                    continue;
                }
                final TypeBuilder sub = new TypeBuilder(stub);
                sub.implementNone();
                final byte[] bytes = sub.writer().toByteArray();
                stream.write(bytes);
                stream.closeEntry();
            }
            final ZipEntry entry = new ZipEntry(Type.getType(real).getInternalName() + ".class");
            final ClassWriter writer = ClassTransformer.transform(real);
            final byte[] bytes = writer.toByteArray();
            try {
                stream.putNextEntry(entry);
            } catch (ZipException ex) {
                continue;
            }
            stream.write(bytes);
            stream.closeEntry();
        }
    }
    
    public void compileAll(ZipOutputStream stream) throws IOException {
        for (final TypeBuilder builder : context.internalBuilders) {
            final byte[] bytes = builder.writer().toByteArray();
            final ZipEntry entry = new ZipEntry(builder.getType().internal() + ".class");
            try {
                stream.putNextEntry(entry);
            } catch (ZipException ex) {
                continue;
            }
            stream.write(bytes);
            stream.closeEntry();
        }
    }
    
    public boolean compile(OutputStream stream) throws IOException {
        if (context.internalBuilders.isEmpty()) return false;
        final TypeBuilder builder = context.internalBuilders.remove(0);
        final byte[] bytes = builder.writer().toByteArray();
        if (stream instanceof ZipOutputStream zip) {
            final ZipEntry entry = new ZipEntry(builder.getType().internal() + ".class");
            zip.putNextEntry(entry);
            zip.write(bytes);
            zip.closeEntry();
        } else stream.write(bytes);
        return !context.internalBuilders.isEmpty();
    }
    
    public void compileAll(File directory) throws IOException {
        if (!directory.exists()) directory.mkdirs();
        assert directory.isDirectory();
        for (final TypeBuilder builder : context.internalBuilders) {
            final File file = new File(directory, builder.getType().getTypeName() + ".class");
            if (!file.exists()) file.createNewFile();
            try (final OutputStream stream = new FileOutputStream(file)) {
                final byte[] bytes = builder.writer().toByteArray();
                stream.write(bytes);
            } catch (IOException exception) {
                throw new Error("Unable to write output of '" + builder.getType().name() + "'", exception);
            }
        }
    }
    
    public void compileKit(File directory) throws IOException {
        if (!directory.exists()) directory.mkdirs();
        assert directory.isDirectory();
        final Class<?>[] library = new Class[]{array.class, decimal.class, error.class, integer.class, number.class, object.class, object.object_0.class, pointer.class, string.class, system.class, type.class, InternalAccess.class, InternalAccess.AccessUnsafe.class};
        for (final Class<?> real : library) {
            final TypeStub type = TypeStub.of(real);
            for (final FunctionStub method : type.methods()) {
                final TypeStub stub = method.getFunctionalInterface();
                final File file = new File(directory, stub.getTypeName() + ".class");
                if (!file.exists()) file.createNewFile();
                try (final OutputStream stream = new FileOutputStream(file)) {
                    final TypeBuilder sub = new TypeBuilder(stub);
                    sub.implementNone();
                    final byte[] bytes = sub.writer().toByteArray();
                    stream.write(bytes);
                } catch (IOException exception) {
                    throw new Error("Unable to write output of '" + real.getSimpleName() + "'", exception);
                }
            }
            final File file = new File(directory, real.getTypeName() + ".class");
            if (!file.exists()) file.createNewFile();
            try (final OutputStream stream = new FileOutputStream(file)) {
                final ClassWriter writer = ClassTransformer.transform(real);
                final byte[] bytes = writer.toByteArray();
                stream.write(bytes);
            } catch (IOException exception) {
                throw new Error("Unable to write output of '" + real.getSimpleName() + "'", exception);
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
