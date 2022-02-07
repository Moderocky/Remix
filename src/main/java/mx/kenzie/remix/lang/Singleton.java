package mx.kenzie.remix.lang;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.parser.ConsumerFlag;

public interface Singleton extends Element {
    
    @Override
    default boolean isValid(Context context) {
        return context.hasAllFlags(this.flag());
    }
    
    @Override
    default boolean matches(Context context, String string) {
        return true;
    }
    
    @Override
    default void write(Context context, String string) {
        context.removeFlags(this.flag());
        this.writeSingle(context, string);
    }
    
    void writeSingle(Context context, String string);
    
    ConsumerFlag flag();
    
}
