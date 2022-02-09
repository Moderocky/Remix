package mx.kenzie.remix.lang.code;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.lang.Keyword;
import mx.kenzie.remix.parser.AreaFlag;

public class KeywordDuplicate implements Keyword, Element {
    
    @Override
    public boolean isValid(Context context) {
        return context.hasAllFlags(AreaFlag.INSTRUCTION_AREA);
    }
    
    @Override
    public boolean matches(Context context, String string) {
        return "dup".equals(string) && context.check() != null;
    }
    
    @Override
    public void write(Context context, String string) {
        context.write(visitor -> visitor.visitInsn(89));
        context.push(context.check());
    }
}
