package mx.kenzie.remix.lang.type;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.lang.Keyword;
import mx.kenzie.remix.parser.AreaFlag;
import mx.kenzie.remix.parser.ConsumerFlag;
import mx.kenzie.remix.parser.ParseMode;

public class KeywordType implements Keyword, Element {
    
    @Override
    public boolean isValid(Context context) {
        return !context.hasAnyFlags(AreaFlag.HEADER, AreaFlag.BODY_TYPE);
    }
    
    @Override
    public boolean matches(Context context, String string) {
        return string.equals("type");
    }
    
    @Override
    public ParseMode nextMode(ParseMode previous) {
        return ParseMode.WORD;
    }
    
    @Override
    public boolean hasHeader() {
        return true;
    }
    
    @Override
    public boolean hasBody() {
        return true;
    }
    
    @Override
    public void write(Context context, String string) {
        context.addFlags(AreaFlag.HEADER_TYPE, ConsumerFlag.HEADER_TYPE_NAME);
    }
    
    @Override
    public void open(Context context, String string) {
        context.removeFlags(AreaFlag.HEADER_TYPE);
        context.addFlags(AreaFlag.BODY_TYPE);
    }
    
    @Override
    public void close(Context context, String string) {
        context.removeFlags(AreaFlag.BODY_TYPE);
    }
    
}
