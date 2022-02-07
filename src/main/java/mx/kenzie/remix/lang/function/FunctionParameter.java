package mx.kenzie.remix.lang.function;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.lang.Singleton;
import mx.kenzie.remix.meta.TypeStub;
import mx.kenzie.remix.parser.ConsumerFlag;

public class FunctionParameter implements Singleton, Element {
    
    @Override
    public void writeSingle(Context context, String string) {
        final TypeStub type = context.findType(string);
        context.currentFunction().addParameter(type);
        context.addFlags(ConsumerFlag.HEADER_FUNC_PARAM_NAME);
    }
    
    @Override
    public ConsumerFlag flag() {
        return ConsumerFlag.HEADER_FUNC_PARAM_TYPE;
    }
    
}
