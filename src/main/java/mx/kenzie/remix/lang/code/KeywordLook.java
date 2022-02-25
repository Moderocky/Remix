package mx.kenzie.remix.lang.code;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.lang.Keyword;
import mx.kenzie.remix.meta.TypeStub;
import mx.kenzie.remix.parser.AreaFlag;
import mx.kenzie.remix.parser.ConsumerFlag;


public class KeywordLook implements Keyword, Element {
    
    @Override
    public boolean isValid(Context context) {
        return context.hasAllFlags(AreaFlag.INSTRUCTION_AREA);
    }
    
    @Override
    public boolean matches(Context context, String string) {
        if (!"look".equals(string)) return false;
        if (context.check() == null) {
            context.error("Looking requires a preceding value to convert.");
            return false;
        }
        return true;
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
        context.removeFlags(AreaFlag.INSTRUCTION_AREA);
        context.addFlags(AreaFlag.BODY_LOOK);
        context.openTracker();
    }
    
    @Override
    public void close(Context context, String string) {
        context.removeFlags(AreaFlag.BODY_LOOK);
        context.addFlags(AreaFlag.INSTRUCTION_AREA);
        final int amount = context.closeTracker();
        final TypeStub stub;
    }
}
