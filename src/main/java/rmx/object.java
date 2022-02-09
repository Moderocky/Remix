package rmx;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public interface object extends Serializable {
    
    object INSTANCE = new object() {};
    
    default pointer Pointer() {
        return new pointer(system.system.getAddress(this));
    }
    
    default byte[] toByteArray() throws IOException {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try (final ObjectOutputStream object = new ObjectOutputStream(stream)) {
            object.writeObject(this);
        }
        return stream.toByteArray();
    }
    
    default integer Frozen() {
        return integer.ZERO;
    }
    
    default void Freeze() {
    }
    
    default object Add(object object) {
        new error(this.Type().String() + " does not support addition.").Throw();
        return this;
    }
    
    default String String() {
        return this.toString();
    }
    
    default type Type() {
        return new type(this.getClass());
    }
    
    default object Clone() throws InstantiationException {
        return system.system().Clone(this);
    }
    
    default object Push(object object) {
        new error(this.Type().String() + " does not support push.").Throw();
        return this;
    }
    
    default object Pull(object object) {
        new error(this.Type().String() + " does not support pull.").Throw();
        return this;
    }
    
    default int booleanValue() {
        return this.Boolean().booleanValue();
    }
    
    default integer Boolean() {
        return new integer(1);
    }
    
    default integer Equals(object object) {
        if (this.equals(object)) return integer.ONE;
        return integer.ZERO;
    }
    
}
