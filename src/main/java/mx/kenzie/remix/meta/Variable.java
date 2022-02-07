package mx.kenzie.remix.meta;

import mx.kenzie.remix.compiler.Context;

public interface Variable {
    
    static Variable create(String name, TypeStub type) {
        return new SimpleVariable(name, type);
    }
    
    static Variable internal(TypeStub type) {
        return new InternalVariable(type);
    }
    
    static Variable this0(TypeStub type) {
        return new ThisVariable(type);
    }
    
    String name();
    
    TypeStub type();
    
    default int slot(Context context) {
        return context.slot(this);
    }
    
}

record SimpleVariable(String name, TypeStub type) implements Variable {}

record InternalVariable(TypeStub type) implements Variable {
    @Override
    public String name() {
        return null;
    }
}

record ThisVariable(TypeStub type) implements Variable {
    
    @Override
    public String name() {
        return "this";
    }
    
    @Override
    public int slot(Context context) {
        return 0;
    }
}
