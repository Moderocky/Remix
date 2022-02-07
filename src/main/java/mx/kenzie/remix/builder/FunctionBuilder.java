package mx.kenzie.remix.builder;

import mx.kenzie.remix.meta.MethodStub;
import mx.kenzie.remix.meta.TypeStub;
import mx.kenzie.remix.meta.Variable;

import java.util.ArrayList;
import java.util.List;

public class FunctionBuilder implements Builder {
    
    protected final List<Variable> variables;
    protected MethodStub stub;
    protected boolean returnSet;
    
    public FunctionBuilder(TypeStub owner, String name) {
        this.stub = new MethodStub(owner, TypeStub.of(Object.class), name);
        this.variables = new ArrayList<>();
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
        this.stub = new MethodStub(stub.owner(), stub.result(), stub.name(), this.addAny(stub.parameters(), parameter));
    }
    
    public void setReturnType(TypeStub result) {
        if (!returnSet) {
            this.forceReturnType(result);
            this.returnSet = true;
            return;
        }
        final TypeStub common = stub.result().common(result);
        this.stub = new MethodStub(stub.owner(), common, stub.name(), stub.parameters());
    }
    
    public void forceReturnType(TypeStub result) {
        this.stub = new MethodStub(stub.owner(), result, stub.name(), stub.parameters());
    }
    
}
