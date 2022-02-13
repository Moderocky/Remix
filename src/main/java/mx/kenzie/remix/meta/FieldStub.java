package mx.kenzie.remix.meta;

import org.objectweb.asm.MethodVisitor;

import java.lang.reflect.Field;
import java.util.function.Consumer;

public record FieldStub(TypeStub owner, TypeStub type, String name) {
    
    public FieldStub(Field field) {
        this(TypeStub.of(field.getDeclaringClass()), TypeStub.of(field.getType()), field.getName());
    }
    
    public Consumer<MethodVisitor> set() {
        return visitor -> visitor.visitFieldInsn(181, owner.internal(), name, type.toASM().getDescriptor());
    }
    
    public Consumer<MethodVisitor> get() {
        return visitor -> visitor.visitFieldInsn(180, owner.internal(), name, type.toASM().getDescriptor());
    }
    
}
