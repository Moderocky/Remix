package mx.kenzie.remix.lang.code;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.lang.Keyword;
import mx.kenzie.remix.meta.TypeStub;
import mx.kenzie.remix.parser.AreaFlag;

public class KeywordSwap implements Keyword, Element {
    
    @Override
    public boolean isValid(Context context) {
        return context.hasAllFlags(AreaFlag.INSTRUCTION_AREA);
    }
    
    @Override
    public boolean matches(Context context, String string) {
        if (!"swap".equals(string)) return false;
        if (context.check() == null) {
            context.error("Swapping requires two preceding values to swap.");
            return false;
        }
        return true;
    }
    
    @Override
    public void write(Context context, String string) {
        context.write(visitor -> visitor.visitInsn(95));
        final TypeStub[] stubs = context.pop(2);
        context.push(stubs[1]);
        context.push(stubs[0]);
    }
}
