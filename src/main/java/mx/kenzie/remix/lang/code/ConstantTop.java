package mx.kenzie.remix.lang.code;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.lang.Keyword;
import mx.kenzie.remix.meta.TypeStub;
import mx.kenzie.remix.parser.AreaFlag;

public class ConstantTop implements Keyword, Element {
    
    @Override
    public boolean isValid(Context context) {
        return context.hasAllFlags(AreaFlag.INSTRUCTION_AREA);
    }
    
    @Override
    public boolean matches(Context context, String string) {
        return "top".equals(string);
    }
    
    @Override
    public void write(Context context, String string) {
        final TypeStub stub = TypeStub.of(rmx.object.class);
        context.write(stub.zeroInstance());
        context.push(stub);
    }
}
