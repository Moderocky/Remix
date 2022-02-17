package mx.kenzie.remix.lang.comment;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.parser.AreaFlag;

public class CommentLine implements Element {
    @Override
    public boolean isValid(Context context) {
        return context.hasAllFlags(AreaFlag.COMMENT);
    }
    
    @Override
    public boolean matches(Context context, String string) {
        return true;
    }
    
    @Override
    public void write(Context context, String string) {
        context.removeFlags(AreaFlag.COMMENT);
    }
}
