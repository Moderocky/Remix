package mx.kenzie.remix.lang.code;

import mx.kenzie.remix.compiler.Bookmark;
import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.lang.Keyword;
import mx.kenzie.remix.parser.AreaFlag;
import org.objectweb.asm.Label;

public class KeywordBreak implements Keyword, Element {
    
    @Override
    public boolean isValid(Context context) {
        return context.hasAllFlags(AreaFlag.BODY_BRANCH);
    }
    
    @Override
    public boolean matches(Context context, String string) {
        return "break".equals(string);
    }
    
    @Override
    public void write(Context context, String string) {
        final Bookmark bookmark = context.bookmark();
        final Label label = bookmark.end();
        context.write(visitor -> visitor.visitJumpInsn(167, label));
        if (context.getTracker() != 0) context.fail("Unbalanced stack before branch break.");
    }
}
