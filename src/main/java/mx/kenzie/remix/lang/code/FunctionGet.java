package mx.kenzie.remix.lang.code;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.meta.FunctionStub;
import mx.kenzie.remix.meta.TypeStub;
import mx.kenzie.remix.parser.AreaFlag;
import mx.kenzie.remix.parser.RemixParser;

public class FunctionGet implements Element {
    
    @Override
    public boolean isValid(Context context) {
        return context.hasAllFlags(AreaFlag.INSTRUCTION_AREA);
    }
    
    @Override
    public boolean matches(Context context, String string) {
        if (!string.startsWith(".")) return false;
        if (context.upcoming() == RemixParser.DOWN) return false;
        if (context.check() == null) {
            context.error("Function calls require a preceding value to call from.");
            return false;
        }
        return true;
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
