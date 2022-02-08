package mx.kenzie.remix.lang.function;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.lang.Keyword;
import mx.kenzie.remix.parser.AreaFlag;
import mx.kenzie.remix.parser.ConsumerFlag;

public class KeywordFunction implements Keyword, Element {
    
    @Override
    public boolean isValid(Context context) {
        if (!context.hasAllFlags(AreaFlag.BODY_TYPE)) return false;
        return !context.hasAnyFlags(AreaFlag.HEADER);
    }
    
    @Override
    public boolean matches(Context context, String string) {
        return string.equals("func") && context.check() == null;
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
        context.addFlags(AreaFlag.HEADER_FUNC);
        context.addFlags(ConsumerFlag.HEADER_FUNC_NAME);
    }
    
    @Override
    public void open(Context context) {
        if (context.hasAnyFlags(ConsumerFlag.HEADER_FUNC_NAME))
            context.error("Function was given no name.");
        // todo maybe functional interface members have no name?
        if (context.hasAnyFlags(ConsumerFlag.HEADER_FUNC_PARAM_NAME))
            context.error("Function has trailing parameter type.");
        context.removeFlags(AreaFlag.HEADER_FUNC, ConsumerFlag.HEADER_FUNC_PARAM_TYPE);
        context.addFlags(AreaFlag.BODY_FUNC, AreaFlag.INSTRUCTION_AREA);
    }
    
    @Override
    public void close(Context context) {
        context.removeFlags(AreaFlag.BODY_FUNC, AreaFlag.INSTRUCTION_AREA);
        context.empty();
    }
}
