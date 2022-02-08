package mx.kenzie.remix.builder;

import mx.kenzie.remix.meta.TypeStub;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;

public class TypeBuilder implements Builder {
    
    protected final List<FunctionBuilder> functions = new ArrayList<>();
    protected final List<FieldBuilder> fields = new ArrayList<>();
    protected TypeStub stub;
    // todo field var builders
    
    public TypeBuilder(String name) {
        this.stub = TypeStub.of(name, TypeStub.of(Object.class), TypeStub.of(rmx.object.class));
    }
    
    public TypeStub getType() {
        return stub;
    }
    
    public void extend(TypeStub stub) {
        this.stub.setSuperclass(stub);
    }
    
    public void implement(TypeStub stub) {
        final TypeStub[] interfaces = this.add(this.stub.interfaces(), stub);
        this.stub.setInterfaces(interfaces);
    }
    
    public FunctionBuilder startFunction(String name, int modifiers) {
        final FunctionBuilder builder = new FunctionBuilder(modifiers, stub, name);
        this.functions.add(builder);
        return builder;
    }
    
    public FieldBuilder startField(int modifiers, String name, TypeStub type) {
        final FieldBuilder builder = new FieldBuilder(modifiers, stub, name, type);
        this.fields.add(builder);
        return builder;
    }
    
    public ClassWriter writer() {
        final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        writer.visit(Opcodes.V17, stub.modifiers(), stub.internal(), null, stub.superName(), stub.interfaceNames());
        for (final FieldBuilder field : fields) {
            final FieldVisitor visitor = writer.visitField(field.modifiers, field.stub.name(), field.stub.type().toASM()
                .getDescriptor(), null, null);
            field.accept(visitor);
        }
        for (final FunctionBuilder function : functions) {
            final MethodVisitor visitor = writer.visitMethod(function.stub.modifiers(), function.stub.name(), function.stub.descriptor(), null, new String[0]);
            function.accept(visitor);
        }
        return writer;
    }
    
}
