package mx.kenzie.remix.builder;

import mx.kenzie.remix.meta.FieldStub;
import mx.kenzie.remix.meta.TypeStub;
import org.objectweb.asm.FieldVisitor;

import java.util.function.Consumer;

public class FieldBuilder implements Builder, Consumer<FieldVisitor> {
    
    protected final int modifiers;
    protected FieldStub stub;
    
    public FieldBuilder(int modifiers, TypeStub owner, String name, TypeStub type) {
        this.stub = new FieldStub(owner, type, name);
        this.modifiers = modifiers;
    }
    
    @Override
    public void accept(FieldVisitor visitor) {
        visitor.visitEnd();
    }
    
    public FieldStub getStub() {
        return stub;
    }
}
