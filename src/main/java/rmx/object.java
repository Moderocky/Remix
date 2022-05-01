package rmx;

import mx.kenzie.remix.meta.Operator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public interface object extends Serializable {
    
    object INSTANCE = new object_0();
    
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
    
    default object Clone() throws InstantiationException {
        return system.system().Clone(this);
    }
    
    @Operator("+")
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
    
    @Operator("-")
    default object Sub(object object) {
        new error(this.Type().String() + " does not support subtraction.").Throw();
        return this;
    }
    
    @Operator("*")
    default object Mul(object object) {
        new error(this.Type().String() + " does not support multiplication.").Throw();
        return this;
    }
    
    @Operator("/")
    default object Div(object object) {
        new error(this.Type().String() + " does not support division.").Throw();
        return this;
    }
    
    @Operator("<<")
    default object Push(object object) {
        new error(this.Type().String() + " does not support push.").Throw();
        return this;
    }
    
    @Operator(">>")
    default object Pull(object object) {
        new error(this.Type().String() + " does not support pull.").Throw();
        return this;
    }
    
    @Operator("!")
    default integer Neg() {
        if (this.booleanValue() > 0) return integer.ZERO;
        return integer.ONE;
    }
    
    default int booleanValue() {
        return this.Bool().booleanValue();
    }
    
    @Operator("?")
    default integer Bool() {
        return new integer(1);
    }
    
    @Operator("=")
    default integer Equals(object object) {
        if (this.equals(object)) return integer.ONE;
        return integer.ZERO;
    }
    
    @Operator("&")
    default object And(object object) {
        new error(this.Type().String() + " does not support and.").Throw();
        return this;
    }
    
    @Operator("|")
    default object Or(object object) {
        new error(this.Type().String() + " does not support or.").Throw();
        return this;
    }
    
    class object_0 implements object {
    }
    
}
