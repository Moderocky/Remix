package rmx;

import mx.kenzie.jupiter.stream.InternalAccess;
import sun.misc.Unsafe;

import java.io.PrintStream;
import java.lang.reflect.Field;

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
    
    public object Allocate(Class<?> type) throws InstantiationException {
        if (!object.class.isAssignableFrom(type)) this.Error("Unable to allocate non-remix object.");
        final object thing = (object) unsafe.allocateInstance(type);
        for (final Field field : type.getFields()) {
            if (field.getType().isPrimitive()) continue;
            final long offset = unsafe.objectFieldOffset(field);
            if (!object.class.isAssignableFrom(field.getType()))
                this.unsafe.putObject(thing, offset, unsafe.allocateInstance(field.getType()));
            else this.unsafe.putObject(thing, offset, this.Allocate(field.getType()));
        }
        return thing;
    }
    
    public object Clone(object object) throws InstantiationException {
        final object copy = (object) unsafe.allocateInstance(object.getClass());
        final long amount = this.getSize(object);
        final long from = object.Pointer();
        final long to = copy.Pointer();
        this.unsafe.copyMemory(from + 12, to + 12, amount);
        return copy;
    }
    
    public long GetPointer(object object) {
        return this.getAddress(object);
    }
    
    public object GetObject(long pointer) {
        return (object) this.getObject(pointer);
    }
    
    public Byte Box(byte b) {
        return b;
    }
    
    public Short Box(short s) {
        return s;
    }
    
    public Integer Box(int i) {
        return i;
    }
    
    public Long Box(long l) {
        return l;
    }
    
    public Float Box(float f) {
        return f;
    }
    
    public Double Box(double d) {
        return d;
    }
    
    public Character Box(char c) {
        return c;
    }
    
    public Boolean Box(boolean z) {
        return z;
    }
    
    @Override
    public boolean Frozen() {
        return true;
    }
    
    @Override
    public type Type() {
        return new type(system.class);
    }
    
}
