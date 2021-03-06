package mx.kenzie.remix.lang.code;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.lang.Keyword;
import mx.kenzie.remix.parser.AreaFlag;
import mx.kenzie.remix.parser.ConsumerFlag;

public class KeywordCast implements Keyword, Element {
    
    @Override
    public boolean isValid(Context context) {
        return context.hasAllFlags(AreaFlag.INSTRUCTION_AREA);
    }
    
    @Override
    public boolean matches(Context context, String string) {
        if (!"cast".equals(string)) return false;
        if (context.check() == null) {
            context.error("Casting requires a preceding value to convert.");
            return false;
        }
        return true;
    }
    
    @Override
    public void write(Context context, String string) {
        context.addFlags(ConsumerFlag.CAST);
    }
}
