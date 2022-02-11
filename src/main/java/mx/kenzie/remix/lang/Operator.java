package mx.kenzie.remix.lang;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.meta.TypeStub;
import org.objectweb.asm.MethodVisitor;

import java.util.function.Consumer;

public interface Operator extends Element {
    
    @Override
    default void write(Context context, String string) {
        context.buffer(this.instruction(context, string), this.pop(), this.result());
    }
    
    Consumer<MethodVisitor> instruction(Context context, String string);
    
    default int pop() {
        return 2;
    }
    
    default TypeStub result() {
        return TypeStub.INTEGER;
    }
    
}
