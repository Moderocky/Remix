package mx.kenzie.remix.lang.literal;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Literal;
import mx.kenzie.remix.meta.TypeStub;
import mx.kenzie.remix.parser.AreaFlag;

public class LiteralString implements Literal<rmx.string> {
    @Override
    public boolean isValid(Context context) {
        return context.hasAnyFlags(AreaFlag.LOAD_VALUE, AreaFlag.LOAD_CONSTANT);
    }
    
    @Override
    public boolean matches(Context context, String string) {
        return string.startsWith("\"") && string.endsWith("\"");
    }
    
    @Override
    public void write(Context context, String string) {
        final String value = this.parse(string).value();
        final TypeStub type = TypeStub.of(rmx.string.class);
        context.write(visitor -> visitor.visitTypeInsn(187, type.internal()));
        context.write(visitor -> visitor.visitInsn(89));
        context.write(visitor -> visitor.visitLdcInsn(value));
        context.write(type.getConstructor(TypeStub.of(String.class)).write());
        context.push(type);
    }
    
    @Override
    public rmx.string parse(String string) {
        return new rmx.string(string.substring(1, string.length() - 1).translateEscapes());
    }
    
    @Override
    public String unwrap(rmx.string string) {
        return '"' + string.value() + '"';
    }
}
