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
        context.addFlags(AreaFlag.BODY_EXIT, AreaFlag.LOAD_VALUE);
        context.openTracker();
    }
    
    @Override
    public void close(Context context, String string) {
        context.removeFlags(AreaFlag.BODY_EXIT, AreaFlag.LOAD_VALUE);
        final int amount = context.closeTracker();
        final TypeStub stub;
        if (amount == 0) stub = TypeStub.of(void.class);
        else stub = context.pop();
        final TypeStub result = context.currentFunction().setReturnType(stub);
        final int offset = result.offset();
        if (amount == 0 && offset != 6) context.write(result.zeroInstance());
        context.write(visitor -> visitor.visitInsn(171 + offset));
    }
}
