package mx.kenzie.remix.compiler;

import mx.kenzie.remix.builder.TypeBuilder;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.meta.TypeStub;
import mx.kenzie.remix.parser.Flag;
import org.objectweb.asm.MethodVisitor;

import java.util.function.Consumer;

public interface Context {
    
    void addFlags(Flag... flags);
    
    void removeFlags(Flag... flags);
    
    Flag[] getFlags();
    
    boolean hasAllFlags(Flag... flags);
    
    boolean hasAnyFlags(Flag... flags);
    
    void write(Consumer<MethodVisitor> consumer);
    
    Element[] availableElements();
    
    default Element findElement(String string) {
        for (final Element element : this.availableElements()) {
            if (!element.matches(this, string)) continue;
            return element;
        }
        return null;
    }
    
    default TypeStub findType(String name) {
        return this.findType(TypeStub.of(name));
    }
    
    default TypeStub findType(String name, TypeStub superclass) {
        return this.findType(TypeStub.of(name, superclass));
    }
    
    default TypeStub findType(String name, TypeStub superclass, TypeStub... interfaces) {
        return this.findType(TypeStub.of(name, superclass, interfaces));
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
    
    TypeStub[] availableTypes();
    
    void registerType(TypeStub type);
    
    TypeBuilder currentType();
    
    TypeBuilder startType(String name);
    
    void endType();
    
}
