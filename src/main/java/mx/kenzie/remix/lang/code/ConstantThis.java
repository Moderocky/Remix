package mx.kenzie.remix.lang.code;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.lang.Keyword;
import mx.kenzie.remix.parser.AreaFlag;

public class ConstantThis implements Keyword, Element {
    
    @Override
    public boolean isValid(Context context) {
        return context.hasAllFlags(AreaFlag.INSTRUCTION_AREA);
    }
    
    @Override
    public boolean matches(Context context, String string) {
        return "this".equals(string);
    }
    
    @Override
    public void write(Context context, String string) {
        context.write(visitor -> visitor.visitVarInsn(25, 0));
        context.push(context.getType());
    }
}
