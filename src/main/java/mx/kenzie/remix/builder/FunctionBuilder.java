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
    protected final FunctionStub stub;
    protected boolean returnSet;
    protected volatile TypeStub prepared;
    protected boolean operator;
    
    public FunctionBuilder(int modifiers, TypeStub owner, String name) {
        this.stub = new FunctionStub(modifiers, owner, TypeStub.of(void.class), name);
        this.variables = new ArrayList<>();
        this.instructions = new ArrayList<>();
    }
    
    public void setOperator(boolean operator) {
        this.operator = operator;
    }
    
    public Variable getVariable(String name) {
        for (final Variable variable : variables) {
            if (name.equals(variable.name())) return variable;
        }
        return null;
    }
    
    public Variable getOrCreateVariable(String name, TypeStub type) {
        for (final Variable variable : variables) {
            if (name.equals(variable.name()) && type.equals(variable.type())) return variable;
        }
        final Variable variable = Variable.create(name, type);
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
    
    public boolean hasVariable(String name) {
        for (final Variable variable : variables) {
            if (name.equals(variable.name())) return true;
        }
        return false;
    }
    
    public synchronized void prepareVariable(TypeStub stub) {
        this.prepared = stub;
    }
    
    public TypeStub getPrepared() {
        final TypeStub stub = this.prepared;
        this.prepared = null;
        return stub;
    }
    
    public int slot(Variable variable) {
        return variables.indexOf(variable);
    }
    
    public void addParameter(TypeStub parameter) {
        this.stub.setParameters(this.addAny(stub.parameters(), parameter));
    }
    
    public TypeStub setReturnType(TypeStub result) {
        assert result != null;
        if (!returnSet) {
            this.forceReturnType(result);
            this.returnSet = true;
            return result;
        }
        final TypeStub common = stub.result().common(result);
        this.stub.setResult(common);
        return common;
    }
    
    public void forceReturnType(TypeStub result) {
        this.stub.setResult(result);
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
