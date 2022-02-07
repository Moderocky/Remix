package mx.kenzie.remix.lang;

public interface Literal<Type> extends Element {
    
    Type parse(String string);
    
    String unwrap(Type type);
    
}
