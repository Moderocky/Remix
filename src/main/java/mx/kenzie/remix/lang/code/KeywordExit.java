package mx.kenzie.remix.lang.code;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.lang.Keyword;
import mx.kenzie.remix.meta.TypeStub;
import mx.kenzie.remix.parser.AreaFlag;


public class KeywordExit implements Keyword, Element {
    
    @Override
    public boolean isValid(Context context) {
        return context.hasAllFlags(AreaFlag.INSTRUCTION_AREA);
    }
    
    @Override
    public boolean matches(Context context, String string) {
        return "exit".equals(string);
    }
    
    @Override
    public boolean hasBody() {
        return true;
    }
    
    @Override
    public void open(Context context, String string) {
        context.addFlags(AreaFlag.RETURN, AreaFlag.LOAD_VALUE);
    }
    
    @Override
    public void close(Context context, String string) {
        context.removeFlags(AreaFlag.RETURN, AreaFlag.LOAD_VALUE);
        final TypeStub stub = context.pop();
        final int offset = context.currentFunction().setReturnType(stub).offset();
        context.write(visitor -> visitor.visitInsn(171 + offset));
    }
}
