package mx.kenzie.remix.lang.operator;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.lang.Singleton;
import mx.kenzie.remix.meta.Operator;
import mx.kenzie.remix.meta.Variable;
import mx.kenzie.remix.parser.ConsumerFlag;

import java.lang.reflect.Modifier;

public class OperatorName implements Singleton, Element {
    
    @Override
    public boolean matches(Context context, String string) {
        if (!Operator.OPERATORS.contains(string)) {
            context.error(string + " is not a valid operator.");
            return false;
        }
        return Singleton.super.matches(context, string);
    }
    
    @Override
    public void writeSingle(Context context, String string) {
        final Variable variable = Variable.this0(context.getType());
        if (string.equals("New")) {
            context.prepareModifier(Modifier.STATIC);
            context.startFunction("<init>");
        } else {
            context.startFunction(string);
            context.currentFunction().setOperator(true);
        }
        context.currentFunction().insertVariable(variable);
        context.addFlags(ConsumerFlag.HEADER_OPER_PARAM_TYPE);
    }
    
    @Override
    public ConsumerFlag flag() {
        return ConsumerFlag.HEADER_OPER_NAME;
    }
    
}
