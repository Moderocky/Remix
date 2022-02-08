package mx.kenzie.remix.meta;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Consumer;

public record FunctionStub(int modifiers, TypeStub owner, TypeStub result, String name, TypeStub... parameters) {
    
    public FunctionStub(Method method) {
        this(method.getModifiers(), TypeStub.of(method.getDeclaringClass()), TypeStub.of(method.getReturnType()), method.getName(), TypeStub.of(method.getParameterTypes()));
    }
    
    public FunctionStub(Constructor<?> constructor) {
        this(constructor.getModifiers(), TypeStub.of(constructor.getDeclaringClass()), TypeStub.of(void.class), "<init>", TypeStub.of(constructor.getParameterTypes()));
    }
    
    public boolean canAccept(TypeStub... parameters) {
        if (Arrays.equals(this.parameters, parameters)) return true;
        for (int i = 0; i < parameters.length; i++) {
            if (!this.parameters[i].canCast(parameters[i])) return false;
        }
        return true;
    }
    
    public Type toASM() {
        final Type[] types = new Type[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            types[i] = parameters[i].toASM();
        }
        return Type.getMethodType(result.toASM(), types);
    }
    
    public Consumer<MethodVisitor> write() {
        if ((modifiers & 0x00000008) != 0)
            return visitor -> visitor.visitMethodInsn(184, owner.internal(), name, this.descriptor(), owner.isInterface());
        if ((modifiers & 0x00000002) != 0 || name.equals("<init>"))
            return visitor -> visitor.visitMethodInsn(183, owner.internal(), name, this.descriptor(), false);
        if (owner.isInterface())
            return visitor -> visitor.visitMethodInsn(185, owner.internal(), name, this.descriptor(), owner.isInterface());
        else
            return visitor -> visitor.visitMethodInsn(182, owner.internal(), name, this.descriptor(), owner.isInterface());
    }
    
    public String descriptor() {
        final Type[] types = new Type[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            types[i] = parameters[i].toASM();
        }
        return Type.getMethodType(result.toASM(), types).getDescriptor();
    }
    
}
