package mx.kenzie.remix.compiler;

import mx.kenzie.remix.meta.FunctionStub;
import mx.kenzie.remix.meta.TypeStub;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClassTransformer extends ClassVisitor {
    
    final String[] interfaces;
    final ClassVisitor visitor;
    
    public ClassTransformer(ClassVisitor visitor, String... interfaces) {
        super(Opcodes.ASM9, visitor);
        this.interfaces = interfaces;
        this.visitor = visitor;
    }
    
    public static ClassWriter transform(final Class<?> real) throws IOException {
        try (final InputStream stream = ClassLoader.getSystemResourceAsStream(real.getName()
            .replace('.', '/') + ".class")) {
            assert stream != null;
            return transform(real, stream.readAllBytes());
        }
    }
    
    public static ClassWriter transform(Class<?> real, byte[] bytes) {
        final TypeStub stub = TypeStub.of(real);
        final List<FunctionStub> list = new ArrayList<>(List.of(stub.methods()));
        list.removeIf(function -> function.owner() != stub);
        list.removeIf(function -> function.name().contains("<"));
        list.removeIf(function -> Modifier.isStatic(function.modifiers()) || Modifier.isPrivate(function.modifiers()));
        final FunctionStub[] functions = list.toArray(new FunctionStub[0]);
        final String[] interfaces = new String[functions.length];
        for (int i = 0; i < functions.length; i++) interfaces[i] = functions[i].getFunctionalInterface().internal();
        final ClassReader reader = new ClassReader(bytes);
        final ClassWriter writer = new ClassWriter(reader, 0);
        final ClassTransformer transformer = new ClassTransformer(writer, interfaces);
        reader.accept(transformer, 0);
        return writer;
    }
    
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        final Set<String> set = new HashSet<>(List.of(interfaces));
        final boolean result = set.addAll(List.of(this.interfaces));
        final String[] array = set.toArray(new String[0]);
        this.visitor.visit(version, access, name, signature, superName, array);
        assert !System.getProperty("strict_append_check", "false")
            .equals("true") || result : "No function descriptions were appended to " + name;
    }
}
