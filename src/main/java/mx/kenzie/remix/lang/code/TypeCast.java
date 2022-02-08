package mx.kenzie.remix.lang.code;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.lang.Singleton;
import mx.kenzie.remix.meta.FunctionStub;
import mx.kenzie.remix.meta.TypeStub;
import mx.kenzie.remix.parser.ConsumerFlag;

public class TypeCast implements Singleton, Element {
    
    @Override
    public void writeSingle(Context context, String string) {
        final TypeStub stub = context.findType(string);
        final TypeStub old = context.check();
        if (old.primitive() && !stub.primitive()) {
            final TypeStub system = TypeStub.of(rmx.system.class);
            final FunctionStub function = system.getMethod("Box", old);
            context.write(system.newInstance());
            if (old.wide()) context.write(visitor -> {
                visitor.visitInsn(91);
                visitor.visitInsn(87);
            });
            else context.write(visitor -> visitor.visitInsn(95));
            context.pop();
            context.write(function.write());
            context.write(visitor -> visitor.visitTypeInsn(192, stub.internal()));
            context.push(stub);
        } else if (old.primitive() && stub.primitive()) {
            final int code = old.conversionCode(stub);
            if (code == 0) return;
            context.pop();
            context.write(visitor -> visitor.visitInsn(code));
            context.push(stub);
        } else if (!old.primitive() && stub.primitive()) {
            final TypeStub number = TypeStub.of(Number.class);
            context.write(visitor -> visitor.visitTypeInsn(192, number.internal()));
            final FunctionStub function = number.getMethod(stub.getTypeName() + "Value");
            context.pop();
            context.write(function.write());
            context.push(stub);
        } else {
            context.write(visitor -> visitor.visitTypeInsn(192, stub.internal()));
        }
    }
    
    @Override
    public ConsumerFlag flag() {
        return ConsumerFlag.CAST;
    }
    
}
