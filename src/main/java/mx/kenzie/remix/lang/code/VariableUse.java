package mx.kenzie.remix.lang.code;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.meta.Variable;
import mx.kenzie.remix.parser.AreaFlag;
import mx.kenzie.remix.parser.ConsumerFlag;

public class VariableUse implements Element {
    
    
    @Override
    public boolean isValid(Context context) {
        if (context.hasAnyFlags(ConsumerFlag.HEADER_VAR_NAME)) return false;
        return context.hasAllFlags(AreaFlag.INSTRUCTION_AREA);
    }
    
    @Override
    public boolean matches(Context context, String string) {
        return context.currentFunction().hasVariable(string);
    }
    
    @Override
    public void write(Context context, String string) {
        final Variable variable = context.currentFunction().getVariable(string);
        assert variable != null;
        context.write(variable.load(context));
        context.push(variable.type());
    }
}
