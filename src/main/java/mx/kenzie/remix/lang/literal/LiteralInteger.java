package mx.kenzie.remix.lang.literal;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Literal;
import mx.kenzie.remix.meta.TypeStub;
import mx.kenzie.remix.parser.AreaFlag;

public class LiteralInteger implements Literal<rmx.integer> {
    @Override
    public boolean isValid(Context context) {
        return context.hasAnyFlags(AreaFlag.INSTRUCTION_AREA);
    }
    
    @Override
    public boolean matches(Context context, String string) {
        if (string.length() > 10) return false;
        final boolean negative = (string.charAt(0) == '-');
        if (string.indexOf('.') > -1) return false;
        if (!negative && string.length() > 3) return false;
        for (final char c : string.toCharArray()) {
            if (c != 45 && c < 48) return false;
            if (c > 57) return false;
        }
        return true;
    }
    
    @Override
    public void write(Context context, String string) {
        final int value = this.parse(string).value();
        final TypeStub type = TypeStub.of(rmx.integer.class);
        context.write(visitor -> visitor.visitTypeInsn(187, type.internal()));
        context.write(visitor -> visitor.visitInsn(89));
        context.write(visitor -> visitor.visitLdcInsn(value));
        context.write(type.getConstructor(TypeStub.of(int.class)).write());
        context.push(type);
    }
    
    @Override
    public rmx.integer parse(String string) {
        return new rmx.integer(Integer.parseInt(string));
    }
    
    @Override
    public String unwrap(rmx.integer b) {
        return b + "";
    }
}
