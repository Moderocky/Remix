package mx.kenzie.remix.lang.operator;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.lang.Keyword;
import mx.kenzie.remix.parser.AreaFlag;
import mx.kenzie.remix.parser.ConsumerFlag;

import java.lang.reflect.Modifier;

public class KeywordOperator implements Keyword, Element {
    
    @Override
    public boolean isValid(Context context) {
        if (!context.hasAllFlags(AreaFlag.BODY_TYPE)) return false;
        return !context.hasAnyFlags(AreaFlag.HEADER);
    }
    
    @Override
    public boolean matches(Context context, String string) {
        return string.equals("oper") && context.check() == null;
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
        context.addFlags(AreaFlag.HEADER_OPER);
        context.addFlags(ConsumerFlag.HEADER_OPER_NAME);
        context.prepareModifier(Modifier.SYNCHRONIZED);
    }
    
    @Override
    public void open(Context context, String string) {
        if (context.hasAnyFlags(ConsumerFlag.HEADER_OPER_NAME))
            context.error("Operator was given no name.");
        if (context.hasAnyFlags(ConsumerFlag.HEADER_OPER_PARAM_NAME))
            context.error("Operator has trailing parameter type.");
        context.removeFlags(AreaFlag.HEADER_OPER, ConsumerFlag.HEADER_OPER_PARAM_TYPE);
        context.addFlags(AreaFlag.BODY_FUNC, AreaFlag.INSTRUCTION_AREA);
    }
    
    @Override
    public void close(Context context, String string) {
        context.removeFlags(AreaFlag.BODY_FUNC, AreaFlag.INSTRUCTION_AREA);
        context.endFunction();
        context.empty();
    }
}
