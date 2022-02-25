package mx.kenzie.remix.meta;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;

public final class FunctionStub {
    
    private final int modifiers;
    private final TypeStub owner;
    private final String name;
    private TypeStub result;
    private TypeStub[] parameters;
    private boolean requiresCast;
    
    public FunctionStub(Method method) {
        this(method.getModifiers(), TypeStub.of(method.getDeclaringClass()), TypeStub.of(method.getReturnType()), method.getName(), TypeStub.of(method.getParameterTypes()));
    }
    
    public FunctionStub(int modifiers, TypeStub owner, TypeStub result, String name, TypeStub... parameters) {
        this.modifiers = modifiers;
        this.owner = owner;
        this.result = result;
        this.name = name;
        this.parameters = parameters;
    }
    
    public FunctionStub(Constructor<?> constructor) {
        this(constructor.getModifiers(), TypeStub.of(constructor.getDeclaringClass()), TypeStub.of(void.class), "<init>", TypeStub.of(constructor.getParameterTypes()));
    }
    
    public void requireCast() {
        this.requiresCast = true;
    }
    
    public boolean requiresCast() {
        return requiresCast;
    }
    
    public TypeStub getFunctionalInterface() {
        return TypeStub.of(this);
    }
    
    public boolean canAccept(TypeStub... parameters) {
        if (parameters.length != this.parameters.length) return false;
        if (Arrays.equals(this.parameters, parameters)) return true;
        for (int i = 0; i < parameters.length; i++) {
            if (!this.parameters[i].canCast(parameters[i])) return false;
        }
        return true;
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
        return this.toASM().getDescriptor();
    }
    
    public Type toASM() {
        final Type[] types = new Type[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            types[i] = parameters[i].toASM();
        }
        return Type.getMethodType(result.toASM(), types);
    }
    
    public int modifiers() {
        return modifiers;
    }
    
    public TypeStub owner() {
        return owner;
    }
    
    public TypeStub result() {
        return result;
    }
    
    public String name() {
        return name;
    }
    
    public TypeStub[] parameters() {
        return parameters;
    }
    
    public void setResult(TypeStub result) {
        this.result = result;
    }
    
    public void setParameters(TypeStub[] parameters) {
        this.parameters = parameters;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(modifiers, owner, result, name, Arrays.hashCode(parameters));
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (FunctionStub) obj;
        return
            Objects.equals(this.result, that.result) &&
                Objects.equals(this.name, that.name) &&
                Arrays.equals(this.parameters, that.parameters);
    }
    
    @Override
    public String toString() {
        return "FunctionStub[" +
            "modifiers=" + modifiers + ", " +
            "owner=" + owner + ", " +
            "result=" + result + ", " +
            "name=" + name + ", " +
            "parameters=" + Arrays.toString(parameters) + ", " +
            "hash=" + System.identityHashCode(this) + ", " + ']';
    }
    
    
}
