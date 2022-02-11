package mx.kenzie.remix.lang.code;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.lang.Keyword;
import mx.kenzie.remix.parser.AreaFlag;
import mx.kenzie.remix.parser.ConsumerFlag;


public class KeywordInstance implements Keyword, Element {
    
    @Override
    public boolean isValid(Context context) {
        if (context.hasAnyFlags(ConsumerFlag.INSTANCE)) return false;
        return context.hasAllFlags(AreaFlag.INSTRUCTION_AREA);
    }
    
    @Override
    public boolean matches(Context context, String string) {
        return "is".equals(string);
    }
    
    @Override
    public void write(Context context, String string) {
        context.addFlags(ConsumerFlag.INSTANCE);
    }
    
}
