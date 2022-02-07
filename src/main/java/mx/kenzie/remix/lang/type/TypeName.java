package mx.kenzie.remix.lang.type;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.lang.Singleton;
import mx.kenzie.remix.parser.ConsumerFlag;

public class TypeName implements Singleton, Element {
    
    @Override
    public void writeSingle(Context context, String string) {
        context.startType(string);
    }
    
    @Override
    public ConsumerFlag flag() {
        return ConsumerFlag.HEADER_TYPE_NAME;
    }
    
}
