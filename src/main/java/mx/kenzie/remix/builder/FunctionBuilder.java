package mx.kenzie.remix.builder;

import mx.kenzie.remix.meta.FunctionStub;
import mx.kenzie.remix.meta.TypeStub;
import mx.kenzie.remix.meta.Variable;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FunctionBuilder implements Builder, Consumer<MethodVisitor> {
    
    protected final List<Variable> variables;
    protected final List<Consumer<MethodVisitor>> instructions;
    protected FunctionStub stub;
    protected boolean returnSet;
    
    public FunctionBuilder(int modifiers, TypeStub owner, String name) {
        this.stub = new FunctionStub(modifiers, owner, TypeStub.of(void.class), name);
        this.variables = new ArrayList<>();
        this.instructions = new ArrayList<>();
    }
    
    public Variable getVariable(String name) {
        for (final Variable variable : variables) {
            if (variable.name().equals(name)) return variable;
        }
        return null;
    }
    
    public Variable getOrCreateVariable(String name) {
        for (final Variable variable : variables) {
            if (variable.name().equals(name)) return variable;
        }
        final Variable variable = Variable.create(name, TypeStub.of(Object.class));
        this.variables.add(variable);
        return variable;
    }
    
    public TypeStub lastParameter() {
        final TypeStub[] stubs = this.stub.parameters();
        return stubs[stubs.length - 1];
    }
    
    public void insertVariable(Variable variable) {
        this.variables.add(variable);
    }
    
    public int slot(Variable variable) {
        return variables.indexOf(variable);
    }
    
    public void addParameter(TypeStub parameter) {
        this.stub = new FunctionStub(stub.modifiers(), stub.owner(), stub.result(), stub.name(), this.addAny(stub.parameters(), parameter));
    }
    
    public TypeStub setReturnType(TypeStub result) {
        if (!returnSet) {
            this.forceReturnType(result);
            this.returnSet = true;
            return result;
        }
        final TypeStub common = stub.result().common(result);
        this.stub = new FunctionStub(stub.modifiers(), stub.owner(), common, stub.name(), stub.parameters());
        return common;
    }
    
    public void forceReturnType(TypeStub result) {
        this.stub = new FunctionStub(stub.modifiers(), stub.owner(), result, stub.name(), stub.parameters());
    }
    
    public void write(Consumer<MethodVisitor> consumer) {
        this.instructions.add(consumer);
    }
    
    @Override
    public void accept(MethodVisitor visitor) {
        visitor.visitCode();
        for (final Consumer<MethodVisitor> instruction : instructions) {
            instruction.accept(visitor);
        }
        visitor.visitInsn(171 + stub.result().offset());
        visitor.visitMaxs(1, 1);
        visitor.visitEnd();
    }
    
    public FunctionStub getStub() {
        return stub;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof FunctionBuilder builder)
            return stub.equals(builder.stub);
        return false;
    }
    
}
