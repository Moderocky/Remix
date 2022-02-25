package mx.kenzie.remix.lang.code;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.meta.FunctionStub;
import mx.kenzie.remix.meta.TypeStub;
import mx.kenzie.remix.parser.AreaFlag;
import mx.kenzie.remix.parser.ParseMode;

public class FunctionCall implements Element {
    
    @Override
    public boolean isValid(Context context) {
        return context.hasAllFlags(AreaFlag.INSTRUCTION_AREA);
    }
    
    @Override
    public boolean matches(Context context, String string) {
        if (!string.startsWith(".")) return false;
        if (context.check() == null) {
            context.error("Function calls require a preceding value to call from.");
            return false;
        }
        return true;
    }
    
    @Override
    public ParseMode nextMode(ParseMode previous) {
        return ParseMode.WORD;
    }
    
    @Override
    public boolean hasBody() {
        return true;
    }
    
    @Override
    public void write(Context context, String string) {
        final String name = string.substring(1).trim();
    }
    
    @Override
    public void open(Context context, String string) {
        context.openTracker();
    }
    
    @Override
    public void close(Context context, String string) {
        final String name = string.substring(1).trim();
        final int amount = context.closeTracker();
        final TypeStub[] arguments = context.pop(amount);
        final FunctionStub function = context.findFunction(name, arguments);
        if (function == null) context.fail("Unmatched function '" + name + "' from " + context.check().name());
        assert function != null;
        context.pop(); // owner
        if (function.requiresCast()) context.write(function.owner().cast());
        context.write(function.write());
        context.push(function.result());
    }
    
}
