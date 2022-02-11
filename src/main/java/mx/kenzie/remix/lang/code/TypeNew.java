package mx.kenzie.remix.lang.code;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.lang.Singleton;
import mx.kenzie.remix.meta.TypeStub;
import mx.kenzie.remix.parser.ConsumerFlag;

public class TypeNew implements Singleton, Element {
    
    @Override
    public boolean matches(Context context, String string) {
        if (!context.hasType(string)) return false;
        return Singleton.super.matches(context, string);
    }
    
    @Override
    public void writeSingle(Context context, String string) {
        final TypeStub stub = context.findType(string);
        context.push(stub);
        if (stub.primitive()) {
            context.write(visitor -> visitor.visitIntInsn(16, 0));
            return;
        }
        context.write(stub.newInstance());
    }
    
    @Override
    public ConsumerFlag flag() {
        return ConsumerFlag.NEW;
    }
    
}
