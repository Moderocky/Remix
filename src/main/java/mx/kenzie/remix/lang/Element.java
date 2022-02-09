package mx.kenzie.remix.lang;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.parser.Flag;
import mx.kenzie.remix.parser.ParseMode;

public interface Element {
    
    boolean isValid(Context context);
    
    boolean matches(Context context, String string);
    
    default ParseMode nextMode(ParseMode previous) {
        return ParseMode.WORD;
    }
    
    default boolean hasHeader() {
        return false;
    }
    
    default boolean hasBody() {
        return false;
    }
    
    default void write(Context context, String string) {
    
    }
    
    default void open(Context context, String string) {
        context.addFlags(this.insideFlags());
    }
    
    default Flag[] insideFlags() {
        return new Flag[0];
    }
    
    default void close(Context context, String string) {
        context.removeFlags(this.insideFlags());
    }
    
}
