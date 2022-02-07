package mx.kenzie.remix.compiler;

import mx.kenzie.remix.builder.FunctionBuilder;
import mx.kenzie.remix.builder.TypeBuilder;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.meta.TypeStub;
import mx.kenzie.remix.meta.Variable;
import mx.kenzie.remix.parser.Flag;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public interface Context {
    
    void addFlags(Flag... flags);
    
    void removeFlags(Flag... flags);
    
    Flag[] getFlags();
    
    boolean hasAllFlags(Flag... flags);
    
    boolean hasAnyFlags(Flag... flags);
    
    void write(Consumer<MethodVisitor> consumer);
    
    default Element findElement(String string) {
        for (final Element element : this.validElements()) {
            if (!element.matches(this, string)) continue;
            return element;
        }
        return null;
    }
    
    default Element[] validElements() {
        final List<Element> list = new ArrayList<>(Arrays.asList(this.availableElements()));
        list.removeIf(element -> !element.isValid(this));
        return list.toArray(new Element[0]);
    }
    
    Element[] availableElements();
    
    default TypeStub findType(String name) {
        return this.findType(TypeStub.of(name));
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
    
    default TypeStub findType(String name, TypeStub superclass) {
        return this.findType(TypeStub.of(name, superclass));
    }
    
    default TypeStub findType(String name, TypeStub superclass, TypeStub... interfaces) {
        return this.findType(TypeStub.of(name, superclass, interfaces));
    }
    
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
    
    int slot(Variable variable);
    
}
