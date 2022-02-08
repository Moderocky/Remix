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

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Consumer;

public class CompileContext implements Context {
    
    protected final List<TypeBuilder> internalBuilders = new ArrayList<>(); // every one that gets created
    protected final List<TypeBuilder> builder = new ArrayList<>();
    protected final Set<TypeStub> types = new HashSet<>();
    protected final Set<FunctionStub> functions = new HashSet<>();
    protected final Set<FieldStub> fields = new HashSet<>();
    protected final List<Flag> flags = new ArrayList<>();
    protected final Element[] elements;
    protected final List<Element> validElements = new ArrayList<>();
    protected final List<FunctionBuilder> function = new ArrayList<>();
    protected final List<TypeStub> stack = new ArrayList<>();
    protected volatile int maxStack;
    protected volatile int modifier;
    private boolean elementsAreValid = false;
    
    public CompileContext(Element... elements) {
        this.elements = elements;
    }
    
    @Override
    public void addFlags(Flag... flags) {
        this.flags.addAll(Arrays.asList(flags));
        this.elementsAreValid = false;
    }
    
    @Override
    public void removeFlags(Flag... flags) {
        this.flags.removeAll(Arrays.asList(flags));
        this.elementsAreValid = false;
    }
    
    @Override
    public Flag[] getFlags() {
        return flags.toArray(new Flag[0]);
    }
    
    @Override
    public boolean hasAllFlags(Flag... flags) {
        for (final Flag flag : flags) {
            if (!this.flags.contains(flag)) return false;
        }
        return true;
    }
    
    @Override
    public boolean hasAnyFlags(Flag... flags) {
        for (final Flag flag : flags) {
            if (this.flags.contains(flag)) return true;
        }
        return false;
    }
    
    @Override
    public void write(Consumer<MethodVisitor> consumer) {
        this.function.get(0).write(consumer);
    }
    
    @Override
    public Element[] validElements() {
        if (!elementsAreValid) {
            this.validElements.clear();
            this.validElements.addAll(Arrays.asList(this.availableElements()));
            this.validElements.removeIf(element -> !element.isValid(this));
            this.elementsAreValid = true;
        }
        return validElements.toArray(new Element[0]);
    }
    
    @Override
    public Element[] availableElements() {
        return elements;
    }
    
    @Override
    public TypeStub[] availableTypes() {
        return types.toArray(new TypeStub[0]);
    }
    
    @Override
    public void registerType(TypeStub type) {
        this.types.add(type);
    }
    
    @Override
    public synchronized FieldBuilder startField(String name) {
        final TypeStub type = this.pop();
        final FieldBuilder builder = this.builder.get(0).startField(this.modifier, name, type);
        this.fields.add(builder.getStub());
        this.modifier = Modifier.PUBLIC;
        return builder;
    }
    
    @Override
    public FieldBuilder currentField() {
        return null;
    }
    
    @Override
    public synchronized FunctionBuilder startFunction(String name) {
        final FunctionBuilder builder = this.builder.get(0).startFunction(name, this.modifier);
        this.function.add(0, builder);
        this.functions.add(builder.getStub());
        this.modifier = Modifier.PUBLIC;
        return builder;
    }
    
    @Override
    public FunctionBuilder currentFunction() {
        if (function.isEmpty()) return null;
        return function.get(0);
    }
    
    @Override
    public void endFunction() {
        if (function.isEmpty()) return;
        this.function.remove(0);
    }
    
    @Override
    public TypeBuilder currentType() {
        if (builder.size() == 0) return null;
        return builder.get(0);
    }
    
    @Override
    public synchronized TypeBuilder startType(String name) {
        final TypeBuilder builder = new TypeBuilder(name);
        builder.getType().modify(this.modifier);
        this.modifier = Modifier.PUBLIC;
        this.types.add(builder.getType());
        this.builder.add(0, builder);
        this.internalBuilders.add(builder);
        return builder;
    }
    
    @Override
    public void endType() {
        this.builder.remove(0);
    }
    
    @Override
    public void error(String message) {
        throw new RuntimeException(message);
        // todo proper error
    }
    
    @Override
    public int slot(Variable variable) {
        return 0;
    }
    
    @Override
    public TypeStub pop() {
        if (stack.isEmpty()) return null;
        return stack.remove(0);
    }
    
    @Override
    public TypeStub check() {
        if (stack.isEmpty()) return null;
        return stack.get(0);
    }
    
    @Override
    public void push(TypeStub stub) {
        this.stack.add(0, stub);
        if (maxStack < stack.size()) maxStack = stack.size();
    }
    
    @Override
    public synchronized void prepareModifier(int modifier) {
        this.modifier |= modifier;
    }
    
    @Override
    public void empty() {
        this.stack.clear();
    }
    
}
