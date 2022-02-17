package mx.kenzie.remix.lang.comment;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.parser.AreaFlag;
import mx.kenzie.remix.parser.ParseMode;

public class SymbolComment implements Element {
    @Override
    public boolean isValid(Context context) {
        return !context.hasAnyFlags(AreaFlag.COMMENT);
    }
    
    @Override
    public boolean matches(Context context, String string) {
        return string.equals("<-");
    }
    
    @Override
    public ParseMode nextMode(ParseMode previous) {
        return ParseMode.LINE;
    }
    
    @Override
    public void write(Context context, String string) {
        context.addFlags(AreaFlag.COMMENT);
    }
}
