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
        if (context.upcoming() != ':') return false;
        return context.check() != null;
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
        context.pop(); // owner
        context.write(function.write());
        context.push(function.result());
    }
    
}