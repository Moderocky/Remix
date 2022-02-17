package mx.kenzie.remix.lang.code;

import mx.kenzie.remix.compiler.Bookmark;
import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.lang.Keyword;
import mx.kenzie.remix.meta.FunctionStub;
import mx.kenzie.remix.meta.TypeStub;
import mx.kenzie.remix.parser.AreaFlag;
import org.objectweb.asm.Label;


public class KeywordWhile implements Keyword, Element {
    
    @Override
    public boolean isValid(Context context) {
        return context.hasAllFlags(AreaFlag.INSTRUCTION_AREA);
    }
    
    @Override
    public boolean matches(Context context, String string) {
        return "while".equals(string);
    }
    
    @Override
    public boolean hasBody() {
        return true;
    }
    
    @Override
    public void write(Context context, String string) {
        final Bookmark bookmark = context.bookmark(this);
        final Label label = bookmark.start();
        context.write(visitor -> visitor.visitLabel(label));
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
        context.addFlags(AreaFlag.BODY_WHILE, AreaFlag.BODY_BRANCH);
    }
    
    @Override
    public void close(Context context, String string) {
        context.removeFlags(AreaFlag.BODY_WHILE, AreaFlag.BODY_BRANCH);
        final int index = context.closeTracker();
        final Bookmark bookmark = context.bookmark();
        final Label start = bookmark.start();
        final Label end = bookmark.end();
        context.write(visitor -> visitor.visitJumpInsn(167, start));
        context.write(visitor -> visitor.visitLabel(end));
        if (index != 0) context.fail("Unbalanced stack in while-branch.");
    }
}
