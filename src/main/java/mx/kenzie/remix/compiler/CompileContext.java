package mx.kenzie.remix.compiler;

import mx.kenzie.remix.builder.FunctionBuilder;
import mx.kenzie.remix.builder.TypeBuilder;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.meta.TypeStub;
import mx.kenzie.remix.meta.Variable;
import mx.kenzie.remix.parser.Flag;
import org.objectweb.asm.MethodVisitor;

import java.util.*;
import java.util.function.Consumer;

public class CompileContext implements Context {
    
    protected final List<TypeBuilder> internalBuilders = new ArrayList<>(); // every one that gets created
    protected final List<TypeBuilder> builder = new ArrayList<>();
    protected final Set<TypeStub> types = new HashSet<>();
    protected final List<Flag> flags = new ArrayList<>();
    protected final Element[] elements;
    protected final List<FunctionBuilder> function = new ArrayList<>();
    
    public CompileContext(Element... elements) {
        this.elements = elements;
    }
    
    @Override
    public void addFlags(Flag... flags) {
        this.flags.addAll(Arrays.asList(flags));
    }
    
    @Override
    public void removeFlags(Flag... flags) {
        this.flags.removeAll(Arrays.asList(flags));
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
        // todo function write instruction
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
    public FunctionBuilder startFunction(String name) {
        final FunctionBuilder builder = this.builder.get(0).startFunction(name);
        this.function.add(0, builder);
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
    public TypeBuilder startType(String name) {
        final TypeBuilder builder = new TypeBuilder(name);
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
    
}
