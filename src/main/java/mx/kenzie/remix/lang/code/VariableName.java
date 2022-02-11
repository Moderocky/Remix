package mx.kenzie.remix.lang.code;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.lang.Singleton;
import mx.kenzie.remix.meta.TypeStub;
import mx.kenzie.remix.meta.Variable;
import mx.kenzie.remix.parser.ConsumerFlag;

public class VariableName implements Singleton, Element {
    
    @Override
    public void writeSingle(Context context, String string) {
    }
    
    @Override
    public ConsumerFlag flag() {
        return ConsumerFlag.HEADER_VAR_NAME;
    }
    
    @Override
    public boolean hasBody() {
        return true;
    }
    
    @Override
    public void open(Context context, String string) {
        context.openTracker();
    }
    
    @Override
    public void close(Context context, String string) {
        final int stack = context.closeTracker();
        final TypeStub type = context.currentFunction().getPrepared();
        final Variable variable = context.currentFunction().getOrCreateVariable(string, type);
        context.currentFunction().insertVariable(variable);
        if (stack == 0) {
            context.write(type.zeroInstance());
        } else {
            final TypeStub thing = context.pop();
            if (!thing.equals(type))
                context.write(visitor -> visitor.visitTypeInsn(192, type.internal()));
        }
        context.write(variable.store(context));
    }
}
