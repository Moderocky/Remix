package mx.kenzie.remix.lang.symbol;

import mx.kenzie.remix.builder.TypeBuilder;
import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.lang.Operator;
import mx.kenzie.remix.meta.TypeStub;
import mx.kenzie.remix.parser.AreaFlag;
import org.objectweb.asm.MethodVisitor;

import java.util.function.Consumer;

public class SymbolOr implements Operator, Element {
    @Override
    public boolean isValid(Context context) {
        return context.hasAllFlags(AreaFlag.INSTRUCTION_AREA);
    }
    
    @Override
    public boolean matches(Context context, String string) {
        if (!string.equals("|")) return false;
        if (context.check() == null) {
            context.error("Or is a binary operator.");
            return false;
        }
        return true;
    }
    
    @Override
    public Consumer<MethodVisitor> instruction(Context context, String string) {
        return TypeBuilder.OR.write();
    }
    
    @Override
    public TypeStub result() {
        return TypeBuilder.OR.result();
    }
}
