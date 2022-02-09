package rmx;

import mx.kenzie.jupiter.stream.InternalAccess;
import sun.misc.Unsafe;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class system extends PrintStream implements InternalAccess.AccessUnsafe, object {
    
    public static final system system = new system();
    
    private final Unsafe unsafe;
    
    public system() {
        super(System.out);
        this.unsafe = this.getUnsafe();
    }
    
    public static system system() {
        return rmx.system.system;
    }
    
    public void Error(Throwable throwable) {
        this.unsafe.throwException(throwable);
    }
    
    public void Error(String message) {
        this.Error(new Exception(message));
    }
    
    public void Print(Object object) {
        this.println(object);
    }
    
    public object Allocate(Class<?> type) throws Throwable {
        if (!object.class.isAssignableFrom(type)) this.Error("Unable to allocate non-remix object.");
        if (type.isInterface()) return this.getInstance(type);
        final object thing = (object) unsafe.allocateInstance(type);
        for (final Field field : type.getFields()) {
            if (Modifier.isStatic(field.getModifiers())) continue;
            if (field.getType().isPrimitive()) continue;
            final long offset = unsafe.objectFieldOffset(field);
            System.out.println(field); // todo
            if (!object.class.isAssignableFrom(field.getType()))
                this.unsafe.putObject(thing, offset, unsafe.allocateInstance(field.getType()));
            else this.unsafe.putObject(thing, offset, this.Allocate(field.getType()));
        }
        return thing;
    }
    
    private <Thing> Thing getInstance(Class<?> type) throws Throwable {
        if (!type.isInterface()) return (Thing) Allocate(type);
        return (Thing) type.getDeclaredField("INSTANCE").get(null);
    }
    
    public object Clone(object object) throws InstantiationException {
        final object copy = (object) unsafe.allocateInstance(object.getClass());
        final long amount = this.getSize(object);
        final long from = object.Pointer().value();
        final long to = copy.Pointer().value();
        this.unsafe.copyMemory(from + 12, to + 12, amount);
        return copy;
    }
    
    public pointer GetPointer(object object) {
        return new pointer(this.getAddress(object));
    }
    
    public object GetObject(pointer pointer) {
        return (object) this.getObject(pointer.value());
    }
    
    @Override
    public integer Frozen() {
        return integer.ONE;
    }
    
    @Override
    public type Type() {
        return new type(system.class);
    }
    
}
