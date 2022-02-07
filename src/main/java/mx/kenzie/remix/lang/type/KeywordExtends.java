package mx.kenzie.remix.lang.type;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.lang.Keyword;
import mx.kenzie.remix.parser.AreaFlag;
import mx.kenzie.remix.parser.ConsumerFlag;

public class KeywordExtends implements Keyword, Element {
    @Override
    public boolean isValid(Context context) {
        if (!context.hasAnyFlags(ConsumerFlag.HEADER_TYPE_NAME, ConsumerFlag.HEADER_TYPE_EXT)) return false;
        return context.hasAllFlags(AreaFlag.HEADER_TYPE);
    }
    
    @Override
    public boolean matches(Context context, String string) {
        return "is".equals(string);
    }
    
    @Override
    public void write(Context context, String string) {
        context.addFlags(ConsumerFlag.HEADER_TYPE_EXT);
    }
    
}
