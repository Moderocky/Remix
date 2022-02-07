package mx.kenzie.remix.lang;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.parser.ConsumerFlag;
import mx.kenzie.remix.parser.Flag;

public interface Singleton extends Element {
    
    @Override
    default boolean isValid(Context context) {
        return context.hasAllFlags(this.flag());
    }
    
    @Override
    default boolean matches(Context context, String string) {
        return true;
    }
    
    ConsumerFlag flag();
    
    void writeSingle(Context context, String string);
    
    @Override
    default void write(Context context, String string) {
        context.removeFlags(this.flag());
        this.writeSingle(context, string);
    }
    
}
