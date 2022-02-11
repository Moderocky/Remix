package mx.kenzie.remix.lang.literal;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Literal;
import mx.kenzie.remix.meta.TypeStub;
import mx.kenzie.remix.parser.AreaFlag;

public class LiteralDecimal implements Literal<rmx.decimal> {
    @Override
    public boolean isValid(Context context) {
        return context.hasAnyFlags(AreaFlag.INSTRUCTION_AREA);
    }
    
    @Override
    public boolean matches(Context context, String string) {
        final TypeStub stub = context.check();
        if (stub == null) return false;
        if (!TypeStub.of(rmx.number.class).canCast(stub)) return false;
        if (string.length() > 15) return false;
        if (string.charAt(0) != '.') return false;
        for (final char c : string.toCharArray()) {
            if (c != '.' && c < 48) return false;
            if (c > 57) return false;
        }
        return true;
    }
    
    @Override
    public void write(Context context, String string) {
        final double value = this.parse(string).value();
        final TypeStub type = TypeStub.of(rmx.decimal.class);
        context.write(visitor -> visitor.visitTypeInsn(187, type.internal()));
        context.write(visitor -> visitor.visitInsn(90));
        context.write(visitor -> visitor.visitInsn(95));
        context.write(visitor -> visitor.visitLdcInsn(value));
        context.write(type.getConstructor(TypeStub.of(rmx.number.class), TypeStub.of(double.class)).write());
        context.push(type);
    }
    
    @Override
    public rmx.decimal parse(String string) {
        return new rmx.decimal(Double.parseDouble("0" + string));
    }
    
    @Override
    public String unwrap(rmx.decimal b) {
        return b + "";
    }
    
}
