package mx.kenzie.remix.lang.function;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.lang.Singleton;
import mx.kenzie.remix.meta.Operator;
import mx.kenzie.remix.meta.Variable;
import mx.kenzie.remix.parser.ConsumerFlag;

public class FunctionName implements Singleton, Element {
    
    @Override
    public boolean matches(Context context, String string) {
        if (Operator.OPERATORS.contains(string)) {
            context.error(string + " is a reserved operator name.");
            return false;
        }
        return Singleton.super.matches(context, string);
    }
    
    @Override
    public void writeSingle(Context context, String string) {
        final Variable variable = Variable.this0(context.getType());
        context.startFunction(string);
        context.currentFunction().insertVariable(variable);
        context.addFlags(ConsumerFlag.HEADER_FUNC_PARAM_TYPE);
    }
    
    @Override
    public ConsumerFlag flag() {
        return ConsumerFlag.HEADER_FUNC_NAME;
    }
    
}
