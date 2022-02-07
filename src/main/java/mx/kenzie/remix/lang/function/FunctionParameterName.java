package mx.kenzie.remix.lang.function;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.lang.Singleton;
import mx.kenzie.remix.meta.TypeStub;
import mx.kenzie.remix.meta.Variable;
import mx.kenzie.remix.parser.ConsumerFlag;

public class FunctionParameterName implements Singleton, Element {
    
    @Override
    public void writeSingle(Context context, String string) {
        final TypeStub type = context.currentFunction().lastParameter();
        final Variable variable = Variable.create(string, type);
        context.currentFunction().insertVariable(variable);
        context.addFlags(ConsumerFlag.HEADER_FUNC_PARAM_TYPE);
    }
    
    @Override
    public ConsumerFlag flag() {
        return ConsumerFlag.HEADER_FUNC_PARAM_NAME;
    }
    
}
