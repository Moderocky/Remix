package mx.kenzie.remix.lang.code;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.meta.TypeStub;
import mx.kenzie.remix.parser.AreaFlag;
import mx.kenzie.remix.parser.ConsumerFlag;

public class VariableType implements Element {
    
    @Override
    public boolean isValid(Context context) {
        if (context.hasAnyFlags(ConsumerFlag.HEADER_VAR_NAME)) return false;
        return context.hasAllFlags(AreaFlag.INSTRUCTION_AREA);
    }
    
    @Override
    public boolean matches(Context context, String string) {
        return context.hasType(string);
    }
    
    @Override
    public boolean hasHeader() {
        return true;
    }
    
    @Override
    public void write(Context context, String string) {
        context.addFlags(ConsumerFlag.HEADER_VAR_NAME);
        final TypeStub stub = context.findType(string);
        assert stub != null;
        context.currentFunction().prepareVariable(stub);
    }
}
