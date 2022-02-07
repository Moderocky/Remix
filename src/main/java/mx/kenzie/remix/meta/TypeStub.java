package mx.kenzie.remix.meta;

import org.objectweb.asm.Type;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TypeStub {
    
    protected final HashSet<MethodStub> methods;
    protected final HashSet<FieldStub> fields;
    protected String name;
    protected TypeStub component;
    protected TypeStub superclass;
    protected TypeStub[] interfaces;
    protected boolean primitive;
    
    protected TypeStub(String name, TypeStub superclass, TypeStub... interfaces) {
        this.name = name;
        this.superclass = superclass;
        this.interfaces = interfaces;
        this.methods = new HashSet<>();
        this.fields = new HashSet<>();
    }
    
    public static TypeStub of(String name, TypeStub superclass, TypeStub... interfaces) {
        return new TypeStub(name, superclass, interfaces);
    }
    
    public static TypeStub of(String name) {
        return new TypeStub(name, TypeStub.of(Object.class));
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
    
    public boolean primitive() {
        return primitive;
    }
    
    public boolean array() {
        return component != null;
    }
    
    public Type toASM() {
        if (this.array()) return Type.getType("[" + component.toASM().getDescriptor());
        return Type.getType("Lrmx/" + name + ";");
    }
    
    public String name() {
        return name;
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
    
    public MethodStub[] methods() {
        return methods.toArray(new MethodStub[0]);
    }
    
    public void addMethod(MethodStub stub) {
        this.methods.add(stub);
    }
    
    public MethodStub addMethod(TypeStub result, String name, TypeStub... parameters) {
        final MethodStub stub = new MethodStub(this, result, name, parameters);
        this.methods.add(stub);
        return stub;
    }
    
    public MethodStub[] getMethods(String name) {
        final List<MethodStub> list = new ArrayList<>();
        for (final MethodStub method : methods) {
            if (method.name().equals(name)) list.add(method);
        }
        return list.toArray(new MethodStub[0]);
    }
    
    public MethodStub getMethod(String name, TypeStub... parameters) {
        for (final MethodStub method : methods) {
            if (!method.name().equals(name)) continue;
            if (Arrays.equals(method.parameters(), parameters)) return method;
        }
        return null;
    }
    
    public void merge(TypeStub stub) {
        this.methods.addAll(stub.methods);
        this.fields.addAll(stub.fields);
    }
    
    public TypeStub common(TypeStub stub) {
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
        return TypeStub.of(Object.class);
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
        TypeStub stub = this;
        TypeStub face = this.superclass;
        while (stub.superclass != null) {
            list.addAll(stub.allInterfaces());
            stub = stub.superclass;
        }
        for (final TypeStub inter : face.interfaces) {
            list.add(inter);
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
    
    static final class SealedTypeStub extends TypeStub {
        
        private static final Map<Class<?>, TypeStub> CLASS_CACHE = new ConcurrentHashMap<>();
        
        public SealedTypeStub(Class<?> thing) {
            super(thing.getName().replace('.', '/'), of(thing.getSuperclass()), of(thing.getInterfaces()));
            CLASS_CACHE.put(thing, this);
            this.primitive = thing.isPrimitive();
            if (thing.isArray()) {
                this.component = TypeStub.of(thing.getComponentType());
            }
            for (final Method method : thing.getMethods()) {
                this.methods.add(new MethodStub(method));
            }
            for (final Field field : thing.getFields()) {
                this.fields.add(new FieldStub(field));
            }
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
        public void merge(TypeStub stub) {
        }
        
    }
    
}
