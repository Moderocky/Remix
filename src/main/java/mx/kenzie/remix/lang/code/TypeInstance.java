package mx.kenzie.remix.lang.code;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.lang.Singleton;
import mx.kenzie.remix.meta.TypeStub;
import mx.kenzie.remix.parser.ConsumerFlag;

public class TypeInstance implements Singleton, Element {
    
    @Override
    public boolean matches(Context context, String string) {
        if (!context.hasType(string)) return false;
        return Singleton.super.matches(context, string);
    }
    
    @Override
    public void writeSingle(Context context, String string) {
        final TypeStub stub = context.findType(string);
        final TypeStub type = TypeStub.of(rmx.integer.class);
        context.write(visitor -> visitor.visitTypeInsn(193, stub.internal()));
        context.pop();
        context.write(type.getConstructor(TypeStub.of(int.class)).write());
        context.push(type);
    }
    
    @Override
    public ConsumerFlag flag() {
        return ConsumerFlag.INSTANCE;
    }
    
}
