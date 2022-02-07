package mx.kenzie.remix.lang.type;

import mx.kenzie.remix.builder.TypeBuilder;
import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.lang.Keyword;
import mx.kenzie.remix.parser.AreaFlag;
import mx.kenzie.remix.parser.ParseMode;

public class KeywordType implements Keyword, Element {
    
    @Override
    public boolean isValid(Context context) {
        return !context.hasAnyFlags(AreaFlag.HEADER, AreaFlag.DECLARE_TYPE);
    }
    
    @Override
    public boolean matches(Context context, String string) {
        return string.equals("type");
    }
    
    @Override
    public boolean hasHeader() {
        return true;
    }
    
    @Override
    public void write(Context context, String string) {
        context.addFlags(AreaFlag.HEADER_TYPE);
    }
    
    @Override
    public void open(Context context) {
        context.removeFlags(AreaFlag.HEADER_TYPE);
        context.addFlags(AreaFlag.DECLARE_TYPE);
        final TypeBuilder builder = context.currentType();
    }
    
    @Override
    public void close(Context context) {
        context.removeFlags(AreaFlag.DECLARE_TYPE);
    }
    
    @Override
    public ParseMode nextMode(ParseMode previous) {
        return ParseMode.WORD;
    }
    
}
