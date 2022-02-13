package mx.kenzie.remix.lang.code;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.meta.FieldStub;
import mx.kenzie.remix.meta.TypeStub;
import mx.kenzie.remix.parser.AreaFlag;
import mx.kenzie.remix.parser.ParseMode;

public class FieldCall implements Element {
    
    @Override
    public boolean isValid(Context context) {
        return context.hasAllFlags(AreaFlag.INSTRUCTION_AREA);
    }
    
    @Override
    public boolean matches(Context context, String string) {
        if (!string.startsWith("$")) return false;
        if (context.check() == null) {
            context.error("Field calls require a preceding value to call from.");
            return false;
        }
        if (context.findField(string.substring(1).trim()) == null) {
            context.error("No local field for '" + string + "' was found.");
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
        final FieldStub field = context.findField(name);
        context.pop(); // owner
        if (arguments.length == 0) {
            context.write(field.get());
            context.push(field.type());
        } else {
            context.write(field.set());
        }
    }
    
}
