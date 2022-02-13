package rmx;

public class integer extends number {
    
    public static final integer ZERO = new integer();
    public static final integer ONE = new integer(1);
    public static final integer M_ONE = new integer(-1);
    
    final int value;
    
    public integer(int value) {
        this.value = value;
    }
    
    public integer() {
        value = 0;
    }
    
    public int value() {
        return value;
    }
    
    public integer Add(object to) {
        if (to instanceof number number) {
            return new integer(value + number.booleanValue());
        } else {
            new error("Unable to add incompatible type to number.").Throw();
        }
        return integer.ZERO;
    }
    
    @Override
    public integer Push(object object) {
        if (object instanceof number number)
            return new integer(value << number.booleanValue());
        new error("Unable to push incompatible type into number.").Throw();
        return this;
    }
    
    @Override
    public integer Pull(object object) {
        if (object instanceof number number)
            return new integer(value >> number.booleanValue());
        new error("Unable to pull incompatible type into number.").Throw();
        return this;
    }
    
    @Override
    public int booleanValue() {
        return value;
    }
    
    @Override
    public integer Equals(object object) {
        if (object instanceof decimal number) {
            return bool((int) number.value == this.value);
        } else if (object instanceof integer number) {
            return bool(number.value == this.value);
        } else if (object instanceof number number) {
            return bool(number.booleanValue() == this.value);
        } else {
            return integer.ZERO;
        }
    }
    
    @Override
    public object And(object object) {
        return new integer(value & object.booleanValue());
    }
    
    @Override
    public object Or(object object) {
        return new integer(value | object.booleanValue());
    }
    
    @Override
    public String toString() {
        return value + "";
    }
    
}
