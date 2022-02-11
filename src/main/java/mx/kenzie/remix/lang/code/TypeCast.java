package mx.kenzie.remix.lang.code;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.lang.Singleton;
import mx.kenzie.remix.meta.TypeStub;
import mx.kenzie.remix.parser.ConsumerFlag;

public class TypeCast implements Singleton, Element {
    
    @Override
    public boolean matches(Context context, String string) {
        if (context.check() == null) return false;
        if (!context.hasType(string)) return false;
        return Singleton.super.matches(context, string);
    }
    
    @Override
    public void writeSingle(Context context, String string) {
        final TypeStub stub = context.findType(string);
        context.pop();
        context.write(visitor -> visitor.visitTypeInsn(192, stub.internal()));
        context.push(stub);
    }
    
    @Override
    public ConsumerFlag flag() {
        return ConsumerFlag.CAST;
    }
    
}
