package mx.kenzie.remix.compiler;

import mx.kenzie.remix.builder.FieldBuilder;
import mx.kenzie.remix.builder.FunctionBuilder;
import mx.kenzie.remix.builder.TypeBuilder;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.meta.FieldStub;
import mx.kenzie.remix.meta.FunctionStub;
import mx.kenzie.remix.meta.TypeStub;
import mx.kenzie.remix.meta.Variable;
import mx.kenzie.remix.parser.Flag;
import org.objectweb.asm.MethodVisitor;

import java.util.function.Consumer;

public interface Context {
    
    void addFlags(Flag... flags);
    
    void removeFlags(Flag... flags);
    
    Flag[] getFlags();
    
    boolean hasAllFlags(Flag... flags);
    
    boolean hasAnyFlags(Flag... flags);
    
    default void buffer(Consumer<MethodVisitor> consumer) {
        this.buffer(consumer, 0, null);
    }
    
    void buffer(Consumer<MethodVisitor> consumer, int pop, TypeStub push);
    
    void emptyBuffer();
    
    void write(Consumer<MethodVisitor> consumer);
    
    default Element findElement(String string) {
        for (final Element element : this.validElements()) {
            if (!element.matches(this, string)) continue;
            return element;
        }
        return null;
    }
    
    Element[] validElements();
    
    Element[] availableElements();
    
    default boolean hasType(String name) {
        for (final TypeStub stub : this.availableTypes()) {
            if (stub.name().equals(name)) return true;
        }
        return false;
    }
    
    TypeStub[] availableTypes();
    
    default TypeStub findType(String name) {
        for (final TypeStub type : this.availableTypes()) {
            if (type.name().equals(name)) return type;
        }
        final TypeStub stub = TypeStub.of(name);
        this.registerType(stub);
        return stub;
    }
    
    void registerType(TypeStub type);
    
    default TypeStub findType(String name, TypeStub superclass) {
        return this.findType(TypeStub.of(name, superclass));
    }
    
    default TypeStub findType(TypeStub stub) {
        for (final TypeStub type : this.availableTypes()) {
            if (!stub.equals(type)) continue;
            type.merge(stub);
            return type;
        }
        this.registerType(stub);
        return stub;
    }
    
    default TypeStub findType(String name, TypeStub superclass, TypeStub... interfaces) {
        return this.findType(TypeStub.of(name, superclass, interfaces));
    }
    
    FieldBuilder startField(String name);
    
    FieldBuilder currentField();
    
    FunctionBuilder startFunction(String name);
    
    FunctionBuilder currentFunction();
    
    void endFunction();
    
    default TypeStub getType() {
        return this.currentType().getType();
    }
    
    TypeBuilder currentType();
    
    TypeBuilder startType(String name);
    
    void endType();
    
    void error(String message);
    
    default void fail(String message) {
        throw new RuntimeException("Compile error: " + message);
    }
    
    void clearErrors();
    
    String[] errors();
    
    int slot(Variable variable);
    
    TypeStub pop();
    
    TypeStub[] pop(int amount);
    
    TypeStub check();
    
    void push(TypeStub stub);
    
    void incrementTracker();
    
    void prepareModifier(int modifier);
    
    void empty();
    
    void setUpcoming(char c);
    
    char upcoming();
    
    FunctionStub findFunction(String name);
    
    FunctionStub findFunction(String name, TypeStub... parameters);
    
    FieldStub findField(String name);
    
    void openTracker();
    
    int closeTracker();
    
    int getTracker();
    
    void open(Element element, String s);
    
    boolean close(Element element, String string);
    
    Bookmark bookmark();
    
    Bookmark bookmark(Element element);
    
    void closeFields();
}
