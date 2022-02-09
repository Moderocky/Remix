package mx.kenzie.remix.lang.code;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.meta.FunctionStub;
import mx.kenzie.remix.meta.TypeStub;
import mx.kenzie.remix.parser.AreaFlag;

public class FunctionGet implements Element {
    
    @Override
    public boolean isValid(Context context) {
        return context.hasAllFlags(AreaFlag.INSTRUCTION_AREA);
    }
    
    @Override
    public boolean matches(Context context, String string) {
        if (!string.startsWith(".")) return false;
        if (context.upcoming() == ':') return false;
        return context.check() != null;
    }
    
    @Override
    public void write(Context context, String string) {
        final String name = string.substring(1).trim();
        final FunctionStub function = context.findFunction(name, new TypeStub[0]);
        context.pop();
        context.write(function.write());
        context.push(function.result());
    }
    
}
