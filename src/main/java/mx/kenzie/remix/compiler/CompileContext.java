package mx.kenzie.remix.compiler;

import mx.kenzie.remix.builder.FieldBuilder;
import mx.kenzie.remix.builder.FunctionBuilder;
import mx.kenzie.remix.builder.TypeBuilder;
import mx.kenzie.remix.lang.Element;
import mx.kenzie.remix.meta.*;
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
    protected final List<BufferEntry> buffer = new ArrayList<>();
    private final TrackerQueue trackers = new TrackerQueue();
    private final List<String> errors = new ArrayList<>();
    private final List<Bookmark> bookmarks = new ArrayList<>();
    protected volatile int maxStack;
    protected volatile int modifier;
    protected boolean bufferReady;
    private boolean elementsAreValid = false;
    private char upcoming;
    
    public CompileContext(TypeStub[] types, Element... elements) {
        this.types.addAll(List.of(types));
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
    public void buffer(Consumer<MethodVisitor> consumer, int pop, TypeStub push) {
        this.buffer.add(new BufferEntry(consumer, pop, push));
        this.bufferReady = false;
    }
    
    @Override
    public void emptyBuffer() {
        if (!bufferReady) return;
        if (buffer.isEmpty()) return;
        for (final BufferEntry entry : buffer) {
            final Consumer<MethodVisitor> action = entry.consumer();
            this.function.get(0).write(action);
            if (entry.pop() > 0) this.pop(entry.pop());
            if (entry.push() != null) this.push(entry.push());
        }
        this.buffer.clear();
    }
    
    @Override
    public void write(Consumer<MethodVisitor> consumer) {
        this.function.get(0).write(consumer);
        this.bufferReady = true;
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
        this.errors.add(message);
    }
    
    @Override
    public void clearErrors() {
        this.errors.clear();
    }
    
    @Override
    public String[] errors() {
        return errors.toArray(new String[0]);
    }
    
    @Override
    public int slot(Variable variable) {
        return this.currentFunction().slot(variable);
    }
    
    @Override
    public TypeStub pop() {
        if (stack.isEmpty()) return null;
        this.trackers.count(-1);
        return stack.remove(0);
    }
    
    @Override
    public TypeStub[] pop(int amount) {
        if (stack.isEmpty()) return new TypeStub[amount];
        final List<TypeStub> stubs = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            stubs.add(0, stack.remove(0));
        }
        this.trackers.count(amount * -1);
        return stubs.toArray(new TypeStub[0]);
    }
    
    @Override
    public TypeStub check() {
        if (stack.isEmpty()) return null;
        return stack.get(0);
    }
    
    @Override
    public void push(TypeStub stub) {
        if (stub == null || stub.equals(TypeStub.of(void.class))) return;
        this.stack.add(0, stub);
        this.trackers.count(1);
        if (maxStack < stack.size()) maxStack = stack.size();
    }
    
    @Override
    public void incrementTracker() {
        this.trackers.count(1);
    }
    
    @Override
    public synchronized void prepareModifier(int modifier) {
        this.modifier |= modifier;
    }
    
    @Override
    public void empty() {
        final int amount = this.stack.size();
        this.stack.clear();
        this.trackers.count(amount * -1);
    }
    
    @Override
    public void setUpcoming(char c) {
        this.upcoming = c;
    }
    
    @Override
    public char upcoming() {
        return upcoming;
    }
    
    @Override
    public FunctionStub findFunction(String name) {
        final TypeStub stub = this.check();
        if (stub.equals(this.getType()))
            return this.findLocalFunction(name);
        final FunctionStub[] functions = stub.getMethods(name);
        if (functions.length > 0) return functions[0];
        return null;
    }
    
    @Override
    public FunctionStub findFunction(String name, TypeStub... parameters) {
        final TypeStub stub = this.check();
        final FunctionStub function;
        
        if (stub.equals(this.getType()))
            function = this.findLocalFunction(name, parameters);
        else function = stub.getMethod(name, parameters);
        if (function != null) return function;
        else if (stub.equals(this.getType()))
            return this.makeFieldFunction(name, parameters);
        return null;
    }
    
    @Override
    public FieldStub findField(String name) {
        for (final FieldStub field : this.fields) {
            if (field.name().equals(name)) return field;
        }
        return null;
    }
    
    @Override
    public void openTracker() {
        trackers.open();
    }
    
    @Override
    public int closeTracker() {
        return trackers.close();
    }
    
    @Override
    public void open(Element element, String string) {
        this.bookmarks.add(0, new Bookmark(element));
        element.open(this, string);
    }
    
    @Override
    public boolean close(Element element, String string) {
        assert !bookmarks.isEmpty();
        final Bookmark bookmark = bookmarks.get(0);
        if (bookmark.element() != element) return false;
        element.close(this, string);
        this.bookmarks.remove(0);
        return true;
    }
    
    @Override
    public Bookmark bookmark() {
        return bookmarks.get(0);
    }
    
    @Override
    public void closeFields() {
        for (final FieldStub field : this.fields) {
            this.makeFieldFunction(field.name());
            this.makeFieldFunction(field.name(), field.type());
        }
    }
    
    private FunctionStub findLocalFunction(String name, TypeStub... parameters) {
        for (final FunctionStub function : this.functions) {
            if (!function.name().equals(name)) continue;
            if (function.canAccept(parameters)) return function;
        }
        return null;
    }
    
    private FunctionStub makeFieldFunction(String name, TypeStub... parameters) {
        if (parameters.length > 1) return null;
        final boolean set = parameters.length == 1;
        for (final FieldStub field : this.fields) {
            if (!field.name().equals(name)) continue;
            if (set && !field.type().canCast(parameters[0])) continue;
            if (this.hasFieldFunction(field, parameters)) continue;
            final TypeBuilder type = this.currentType();
            final FunctionBuilder builder = type.startFunction(name, Modifier.PUBLIC | Modifier.SYNCHRONIZED);
            if (set) {
                builder.addParameter(parameters[0]);
                builder.write(visitor -> visitor.visitVarInsn(25, 0));
                builder.write(visitor -> visitor.visitVarInsn(25, 1));
                builder.write(field.set());
                builder.write(visitor -> visitor.visitInsn(177));
            } else {
                builder.setReturnType(field.type());
                builder.write(visitor -> visitor.visitVarInsn(25, 0));
                builder.write(field.get());
                builder.write(visitor -> visitor.visitInsn(176));
            }
            this.functions.add(builder.getStub());
            return builder.getStub();
        }
        return null;
    }
    
    private boolean hasFieldFunction(FieldStub field, TypeStub... parameters) {
        final boolean set = parameters.length > 0;
        for (final FunctionStub stub : functions) {
            if (!stub.name().equals(field.name())) continue;
            if (stub.parameters().length != parameters.length) continue;
            if (!Arrays.equals(stub.parameters(), parameters)) continue;
            if (!set) return stub.result().equals(field.type());
            return true;
        }
        return false;
    }
    
    private FunctionStub findLocalFunction(String name) {
        for (final FunctionStub function : this.functions) {
            if (!function.name().equals(name)) continue;
            if (function.parameters().length == 0) return function;
        }
        return null;
    }
    
}
