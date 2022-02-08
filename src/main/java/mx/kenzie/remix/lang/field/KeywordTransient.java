package mx.kenzie.remix.lang.field;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.lang.Keyword;
import mx.kenzie.remix.parser.AreaFlag;

import java.lang.reflect.Modifier;

public class KeywordTransient implements Keyword, Element {
    
    @Override
    public boolean isValid(Context context) {
        if (!context.hasAllFlags(AreaFlag.BODY_TYPE)) return false;
        return !context.hasAnyFlags(AreaFlag.HEADER_FUNC, AreaFlag.HEADER, AreaFlag.BODY_FUNC);
    }
    
    @Override
    public boolean matches(Context context, String string) {
        return string.equals("trans") && context.check() == null;
    }
    
    @Override
    public void write(Context context, String string) {
        context.prepareModifier(Modifier.TRANSIENT);
    }
    
}
