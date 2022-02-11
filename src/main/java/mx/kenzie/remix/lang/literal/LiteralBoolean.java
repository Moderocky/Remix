package mx.kenzie.remix.lang.literal;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Literal;
import mx.kenzie.remix.meta.TypeStub;
import mx.kenzie.remix.parser.AreaFlag;
import rmx.integer;

public class LiteralBoolean implements Literal<rmx.integer> {
    @Override
    public boolean isValid(Context context) {
        return context.hasAnyFlags(AreaFlag.INSTRUCTION_AREA);
    }
    
    @Override
    public boolean matches(Context context, String string) {
        return string.equals("true") || string.equals("false");
    }
    
    @Override
    public void write(Context context, String string) {
        final TypeStub type = TypeStub.of(rmx.integer.class);
        if (string.equals("true"))
            context.write(visitor -> visitor.visitFieldInsn(178, type.internal(), "ONE", type.toASM().getDescriptor()));
        else context.write(visitor -> visitor.visitFieldInsn(178, type.internal(), "ZERO", type.toASM()
            .getDescriptor()));
        context.push(type);
    }
    
    @Override
    public rmx.integer parse(String string) {
        if (string.equals("true")) return integer.ONE;
        return integer.ZERO;
    }
    
    @Override
    public String unwrap(rmx.integer b) {
        return b.booleanValue() > 0 ? "true" : "false";
    }
}
