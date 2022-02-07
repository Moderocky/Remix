package mx.kenzie.remix.builder;

import mx.kenzie.remix.meta.TypeStub;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Arrays;

public class TypeBuilder implements Builder {
    
    protected TypeStub stub;
    
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
    
}
