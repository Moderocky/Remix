package mx.kenzie.remix.lang.code;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.lang.Keyword;
import mx.kenzie.remix.meta.FunctionStub;
import mx.kenzie.remix.meta.TypeStub;
import mx.kenzie.remix.parser.AreaFlag;
import org.objectweb.asm.Label;


public class KeywordIf implements Keyword, Element {
    
    @Override
    public boolean isValid(Context context) {
        return context.hasAllFlags(AreaFlag.INSTRUCTION_AREA);
    }
    
    @Override
    public boolean matches(Context context, String string) {
        return "if".equals(string);
    }
    
    @Override
    public boolean hasBody() {
        return true;
    }
    
    @Override
    public void open(Context context, String string) {
        final TypeStub stub = context.pop();
        assert stub.canCastTo(TypeStub.of(rmx.object.class));
        final FunctionStub function = TypeStub.of(rmx.object.class).getMethod("booleanValue");
        context.write(function.write());
        final Label label = context.bookmark().end();
        context.write(visitor -> visitor.visitJumpInsn(153, label));
        context.openTracker();
        context.addFlags(AreaFlag.BODY_IF);
    }
    
    @Override
    public void close(Context context, String string) {
        context.removeFlags(AreaFlag.BODY_IF);
        final int index = context.closeTracker();
        final Label label = context.bookmark().end();
        context.write(visitor -> visitor.visitLabel(label));
        if (index != 0) context.fail("Unbalanced stack in if-branch.");
    }
}
