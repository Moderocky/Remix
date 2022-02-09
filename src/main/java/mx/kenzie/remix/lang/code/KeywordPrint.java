package mx.kenzie.remix.lang.code;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.lang.Keyword;
import mx.kenzie.remix.parser.AreaFlag;

import java.io.PrintStream;

public class KeywordPrint implements Keyword, Element {
    @Override
    public boolean isValid(Context context) {
        return context.hasAllFlags(AreaFlag.INSTRUCTION_AREA);
    }
    
    @Override
    public boolean matches(Context context, String string) {
        return "print".equals(string);
    }
    
    @Override
    public boolean hasBody() {
        return true;
    }
    
    @Override
    public void open(Context context, String string) {
        context.addFlags(AreaFlag.LOAD_VALUE);
        context.write(visitor -> visitor.visitFieldInsn(178, "java/lang/System", "out", PrintStream.class.descriptorString()));
    }
    
    @Override
    public void close(Context context, String string) {
        context.removeFlags(AreaFlag.LOAD_VALUE);
        context.write(visitor ->
            visitor.visitMethodInsn(182, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V", false)
        );
    }
}
