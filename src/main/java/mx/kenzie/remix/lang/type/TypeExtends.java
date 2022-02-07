package mx.kenzie.remix.lang.type;

import mx.kenzie.remix.builder.TypeBuilder;
import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.lang.Singleton;
import mx.kenzie.remix.parser.AreaFlag;
import mx.kenzie.remix.parser.ConsumerFlag;

public class TypeExtends implements Singleton, Element {
    
    @Override
    public ConsumerFlag flag() {
        return ConsumerFlag.HEADER_TYPE_EXT;
    }
    
    @Override
    public void writeSingle(Context context, String string) {
        final TypeBuilder builder = context.currentType();
        builder.extend(context.findType(string));
    }
    
}
