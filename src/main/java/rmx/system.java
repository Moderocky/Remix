package rmx;

import mx.kenzie.jupiter.stream.InternalAccess;
import rmx.func.FN_2479d9_9b75;
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
    
    public static void main(String... arguments) {
        if (arguments.length < 1) return;
        try {
            Class<?> thing = Class.forName(arguments[0], true, rmx.system.class.getClassLoader());
            final FN_2479d9_9b75 object = (FN_2479d9_9b75) system.Allocate(thing);
            object.Main();
        } catch (Throwable exception) {
            system.Error(new error(exception.getMessage()));
        }
    }
    
    public static system system() {
        return rmx.system.system;
    }
    
    public void Exit() {
        System.exit(0);
    }
    
    public void Exit(number number) {
        System.exit(number.booleanValue());
    }
    
    public void Error(string message) {
        this.Error(new error(message));
    }
    
    public void Error(error throwable) {
        this.unsafe.throwException(throwable);
    }
    
    public void Error(String message) {
        this.Error(new error(message));
    }
    
    public void Print(Object object) {
        this.println(object);
    }
    
    public void Print(object object) {
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
            if (!object.class.isAssignableFrom(field.getType()))
                this.unsafe.putObject(thing, offset, unsafe.allocateInstance(field.getType()));
            else this.unsafe.putObject(thing, offset, this.Allocate(field.getType()));
        }
        for (final Field field : type.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) continue;
            if (field.getType().isPrimitive()) continue;
            final long offset = unsafe.objectFieldOffset(field);
            if (!object.class.isAssignableFrom(field.getType()))
                this.unsafe.putObject(thing, offset, field.getType().getConstructor().newInstance());
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
