package mx.kenzie.remix.builder;

import mx.kenzie.remix.meta.MethodStub;
import mx.kenzie.remix.meta.TypeStub;

public class FunctionBuilder implements Builder {
    
    protected MethodStub stub;
    protected boolean returnSet;
    
    public FunctionBuilder(TypeStub owner, String name) {
        this.stub = new MethodStub(owner, TypeStub.of(Object.class), name);
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
