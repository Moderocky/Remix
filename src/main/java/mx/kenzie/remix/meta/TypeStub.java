package mx.kenzie.remix.meta;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class TypeStub implements java.lang.reflect.Type {
    
    protected final HashSet<FunctionStub> methods;
    protected final HashSet<FieldStub> fields;
    protected String name;
    protected TypeStub component;
    protected TypeStub superclass;
    protected TypeStub[] interfaces;
    protected boolean primitive;
    protected boolean face;
    protected int modifiers;
    
    protected TypeStub(String name, TypeStub superclass, TypeStub... interfaces) {
        this.name = name;
        this.superclass = superclass;
        this.interfaces = interfaces;
        this.methods = new HashSet<>();
        this.fields = new HashSet<>();
        this.modifiers = Modifier.PUBLIC;
    }
    
    public static TypeStub of(String name, TypeStub superclass, TypeStub... interfaces) {
        return new TypeStub(name, superclass, interfaces);
    }
    
    public static TypeStub of(String name) {
        return switch (name) {
            case "void" -> TypeStub.of(void.class);
            case "number" -> TypeStub.of(rmx.number.class);
            case "integer" -> TypeStub.of(rmx.integer.class);
            case "decimal" -> TypeStub.of(rmx.decimal.class);
            case "string" -> TypeStub.of(rmx.string.class);
            case "error" -> TypeStub.of(rmx.error.class);
            case "system" -> TypeStub.of(rmx.system.class);
            case "type" -> TypeStub.of(rmx.type.class);
            case "object" -> TypeStub.of(rmx.object.class);
            default -> new TypeStub(name, TypeStub.of(Object.class));
        };
    }
    
    public static TypeStub of(Class<?> thing) {
        if (thing == null) return null;
        if (SealedTypeStub.CLASS_CACHE.containsKey(thing)) {
            return SealedTypeStub.CLASS_CACHE.get(thing);
        }
        return new SealedTypeStub(thing);
    }
    
    public static TypeStub[] of(Class<?>... things) {
        final TypeStub[] stubs = new TypeStub[things.length];
        for (int i = 0; i < things.length; i++) {
            stubs[i] = of(things[i]);
        }
        return stubs;
    }
    
    @Override
    public String getTypeName() {
        return this.toASM().getClassName();
    }
    
    public int conversionCode(TypeStub stub) {
        return 0;
    }
    
    public int modifiers() {
        return modifiers;
    }
    
    public void modify(int modifiers) {
        this.modifiers |= modifiers;
    }
    
    public boolean isInterface() {
        return face;
    }
    
    public void setInterface(boolean value) {
        this.face = value;
    }
    
    public boolean primitive() {
        return primitive;
    }
    
    public boolean array() {
        return component != null;
    }
    
    public String internal() {
        return this.toASM().getInternalName();
    }
    
    public String superName() {
        if (superclass == null) return null;
        return superclass.internal();
    }
    
    public String[] interfaceNames() {
        final String[] strings = new String[interfaces.length];
        for (int i = 0; i < interfaces.length; i++) {
            strings[i] = interfaces[i].internal();
        }
        return strings;
    }
    
    public TypeStub getArrayType() {
        return new ArrayTypeStub(Type.getType("[" + this.toASM().getDescriptor()), this);
    }
    
    public TypeStub getComponentType() {
        return component;
    }
    
    public Type toASM() {
        if (this.array()) return Type.getType("[" + component.toASM().getDescriptor());
        return Type.getType("Lrmx/" + name + ";");
    }
    
    public String name() {
        final int index = name.lastIndexOf('/');
        return name.substring(index + 1);
    }
    
    public TypeStub setName(String name) {
        this.name = name;
        return this;
    }
    
    public TypeStub setSuperclass(TypeStub superclass) {
        this.superclass = superclass;
        return this;
    }
    
    public TypeStub setInterfaces(TypeStub[] interfaces) {
        this.interfaces = interfaces;
        return this;
    }
    
    public TypeStub superclass() {
        return superclass;
    }
    
    public TypeStub[] interfaces() {
        return interfaces;
    }
    
    public FieldStub[] fields() {
        return fields.toArray(new FieldStub[0]);
    }
    
    public void addField(FieldStub stub) {
        this.fields.add(stub);
    }
    
    public FieldStub addField(TypeStub type, String name) {
        final FieldStub stub = new FieldStub(this, type, name);
        this.fields.add(stub);
        return stub;
    }
    
    public FieldStub getField(String name) {
        for (final FieldStub field : fields) {
            if (field.name().equals(name)) return field;
        }
        return null;
    }
    
    public FunctionStub[] methods() {
        return methods.toArray(new FunctionStub[0]);
    }
    
    public void addMethod(FunctionStub stub) {
        this.methods.add(stub);
    }
    
    public FunctionStub addMethod(TypeStub result, String name, TypeStub... parameters) {
        final FunctionStub stub = new FunctionStub(Modifier.PUBLIC, this, result, name, parameters);
        this.methods.add(stub);
        return stub;
    }
    
    public boolean wide() {
        return false;
    }
    
    public int offset() {
        return 5;
    }
    
    public Consumer<MethodVisitor> typeLiteral() {
        return visitor -> visitor.visitLdcInsn(Type.getObjectType(this.internal()));
    }
    
    public Consumer<MethodVisitor> newInstance() {
        return visitor -> {
            visitor.visitTypeInsn(187, this.internal());
            visitor.visitInsn(89);
            visitor.visitMethodInsn(183, this.internal(), "<init>", "()V", false);
        };
    }
    
    public Consumer<MethodVisitor> zeroInstance() {
        return visitor -> {
            visitor.visitMethodInsn(184, "rmx/system", "system", "()Lrmx/system;", false);
            visitor.visitLdcInsn(Type.getObjectType(this.internal()));
            visitor.visitMethodInsn(182, "rmx/system", "Allocate", "(Ljava/lang/Class;)Lrmx/object;", false);
            visitor.visitTypeInsn(192, this.internal());
        };
    }
    
    public FunctionStub[] getMethods(String name) {
        final List<FunctionStub> list = new ArrayList<>();
        for (final FunctionStub method : methods) {
            if (method.name().equals(name)) list.add(method);
        }
        return list.toArray(new FunctionStub[0]);
    }
    
    public FunctionStub getMethod(String name, TypeStub... parameters) {
        final List<FunctionStub> methods = new ArrayList<>(this.methods);
        methods.removeIf(stub -> !stub.name().equals(name));
        for (final FunctionStub method : methods) {
            if (Arrays.equals(method.parameters(), parameters)) return method;
        }
        for (final FunctionStub method : methods) {
            if (method.canAccept(parameters)) return method;
        }
        for (final TypeStub stub : this.interfaces) {
            final FunctionStub method = stub.getMethod(name, parameters);
            if (method != null) return method;
        }
        if (this.superclass != null) {
            return this.superclass.getMethod(name, parameters);
        }
        return null;
    }
    
    public FunctionStub getConstructor(TypeStub... parameters) {
        for (final FunctionStub method : methods) {
            if (!method.name().equals("<init>")) continue;
            if (Arrays.equals(method.parameters(), parameters)) return method;
        }
        for (final FunctionStub method : methods) {
            if (!method.name().equals("<init>")) continue;
            if (method.canAccept(parameters)) return method;
        }
        return null;
    }
    
    public boolean canCastTo(TypeStub stub) {
        return stub.canCast(this);
    }
    
    public boolean canCast(TypeStub stub) {
        if (this.equals(stub)) return true;
        {
            final List<TypeStub> theirs = stub.allSuperclasses();
            if (theirs.contains(this)) return true;
        }
        {
            final List<TypeStub> theirs = stub.allInterfaces();
            return theirs.contains(this);
        }
    }
    
    public void merge(TypeStub stub) {
        this.methods.addAll(stub.methods);
        this.fields.addAll(stub.fields);
    }
    
    public TypeStub common(TypeStub stub) {
        if (stub.primitive) return this;
        if (this.equals(stub)) return this;
        if (this.array() && stub.array()) return TypeStub.of(Object[].class);
        {
            final List<TypeStub> ours, theirs;
            ours = this.allSuperclasses();
            theirs = stub.allSuperclasses();
            for (final TypeStub our : ours) {
                if (theirs.contains(our)) return our;
            }
        }
        {
            final List<TypeStub> ours, theirs;
            ours = this.allSuperclasses();
            theirs = stub.allSuperclasses();
            ours.addAll(this.allInterfaces());
            theirs.addAll(stub.allInterfaces());
            for (final TypeStub our : ours) {
                if (theirs.contains(our)) return our;
            }
        }
        return TypeStub.of(rmx.object.class);
    }
    
    public List<TypeStub> allSuperclasses() {
        final List<TypeStub> list = new ArrayList<>();
        TypeStub stub = this;
        while (stub.superclass != null) {
            list.add(stub.superclass);
            stub = stub.superclass;
        }
        return list;
    }
    
    public List<TypeStub> allInterfaces() {
        final List<TypeStub> list = new ArrayList<>();
        list.addAll(List.of(this.interfaces));
        if (this.superclass != null) list.addAll(this.superclass.allInterfaces());
        for (final TypeStub inter : this.interfaces) {
            list.addAll(inter.allInterfaces());
        }
        return list;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, superclass, Arrays.hashCode(interfaces));
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (TypeStub) obj;
        return Objects.equals(this.name, that.name) &&
            Objects.equals(this.superclass, that.superclass) &&
            Arrays.equals(this.interfaces, that.interfaces);
    }
    
    @Override
    public String toString() {
        return "TypeStub[name=" + name + ']';
    }
    
}

final class ArrayTypeStub extends TypeStub {
    
    private final Type type;
    
    public ArrayTypeStub(Type type, TypeStub component) {
        super(component.name + "[]", TypeStub.of(Object.class));
        this.component = component;
        this.type = type;
    }
    
    @Override
    public boolean array() {
        return true;
    }
    
    @Override
    public Type toASM() {
        return type;
    }
}

final class SealedTypeStub extends TypeStub {
    
    static final Map<Class<?>, TypeStub> CLASS_CACHE = new ConcurrentHashMap<>();
    
    private final Class<?> type;
    
    public SealedTypeStub(Class<?> thing) {
        super(thing.getName().replace('.', '/'), of(thing.getSuperclass()), of(thing.getInterfaces()));
        this.type = thing;
        this.face = thing.isInterface();
        this.modifiers = thing.getModifiers();
        CLASS_CACHE.put(thing, this);
        this.primitive = thing.isPrimitive();
        this.superclass = TypeStub.of(thing.getSuperclass());
        this.interfaces = TypeStub.of(thing.getInterfaces());
        if (thing.isArray()) {
            this.component = TypeStub.of(thing.getComponentType());
        }
        for (final Method method : thing.getMethods()) {
            this.methods.add(new FunctionStub(method));
        }
        for (final Field field : thing.getFields()) {
            this.fields.add(new FieldStub(field));
        }
        for (final Constructor<?> constructor : thing.getConstructors()) {
            this.methods.add(new FunctionStub(constructor));
        }
    }
    
    @Override
    public int conversionCode(TypeStub stub) {
        if (!(stub instanceof SealedTypeStub sealed)) return 0;
        if (!sealed.primitive) return 0;
        if (type == int.class && sealed.type == byte.class) return Opcodes.I2B;
        if (type == int.class && sealed.type == short.class) return Opcodes.I2S;
        if (type == int.class && sealed.type == long.class) return Opcodes.I2L;
        if (type == int.class && sealed.type == float.class) return Opcodes.I2F;
        if (type == int.class && sealed.type == double.class) return Opcodes.I2D;
        return 0;
    }
    
    @Override
    public boolean array() {
        return type.isArray();
    }
    
    @Override
    public TypeStub getArrayType() {
        return TypeStub.of(type.arrayType());
    }
    
    @Override
    public TypeStub getComponentType() {
        return TypeStub.of(type.getComponentType());
    }
    
    @Override
    public Type toASM() {
        return Type.getType(type);
    }
    
    @Override
    public TypeStub setName(String name) {
        return this;
    }
    
    @Override
    public TypeStub setSuperclass(TypeStub superclass) {
        return this;
    }
    
    @Override
    public TypeStub setInterfaces(TypeStub[] interfaces) {
        return this;
    }
    
    @Override
    public boolean wide() {
        return type == long.class || type == double.class;
    }
    
    @Override
    public int offset() {
        if (type == void.class) return 6;
        else if (type == int.class) return 1;
        else if (type == boolean.class) return 1;
        else if (type == byte.class) return 1;
        else if (type == short.class) return 1;
        else if (type == char.class) return 1;
        else if (type == long.class) return 2;
        else if (type == float.class) return 3;
        else if (type == double.class) return 4;
        else return 5;
    }
    
    @Override
    public void merge(TypeStub stub) {
    }
    
}
