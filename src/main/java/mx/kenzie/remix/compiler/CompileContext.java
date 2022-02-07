package mx.kenzie.remix.compiler;

import mx.kenzie.remix.builder.TypeBuilder;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.meta.TypeStub;
import mx.kenzie.remix.parser.Flag;
import org.objectweb.asm.MethodVisitor;

import java.util.*;
import java.util.function.Consumer;

public class CompileContext implements Context {
    
    protected final List<TypeBuilder> builder = new ArrayList<>();
    protected final Set<TypeStub> types = new HashSet<>();
    protected final List<Flag> flags = new ArrayList<>();
    protected final Element[] elements;
    
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
        Arrays.sort(flags);
        for (final Flag flag : this.flags) {
            final int result = Arrays.binarySearch(flags, flag);
            if (result < 0) return false;
        }
        return true;
    }
    
    @Override
    public boolean hasAnyFlags(Flag... flags) {
        Arrays.sort(flags);
        for (final Flag flag : this.flags) {
            final int result = Arrays.binarySearch(flags, flag);
            if (result >= 0) return true;
        }
        return false;
    }
    
    @Override
    public void write(Consumer<MethodVisitor> consumer) {
        // todo
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
    public TypeBuilder currentType() {
        if (builder.size() == 0) return null;
        return builder.get(0);
    }
    
    @Override
    public TypeBuilder startType(String name) {
        final TypeBuilder builder = new TypeBuilder(name);
        this.types.add(builder.getType());
        this.builder.add(0, builder);
        return builder;
    }
    
    @Override
    public void endType() {
        this.builder.remove(0);
    }
    
}
