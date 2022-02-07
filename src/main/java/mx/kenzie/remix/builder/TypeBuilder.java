package mx.kenzie.remix.builder;

import mx.kenzie.remix.meta.TypeStub;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TypeBuilder implements Builder {
    
    protected final List<FunctionBuilder> functions = new ArrayList<>();
    protected TypeStub stub;
    // todo field var builders
    
    public TypeBuilder(String name) {
        this.stub = TypeStub.of(name, TypeStub.of(Object.class), TypeStub.of(Serializable.class));
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
    
    public FunctionBuilder startFunction(String name) {
        return new FunctionBuilder(stub, name);
    }
    
}
