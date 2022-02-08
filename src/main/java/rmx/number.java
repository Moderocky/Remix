package rmx;

public class number implements object {
    
    public number() {
    }
    
    static integer bool(boolean b) {
        if (b) return integer.ONE;
        return integer.ZERO;
    }
    
    public number Add(object to) {
        if (to instanceof number number && number.getClass() != rmx.number.class) {
            return number.Add(this);
        } else {
            new error("Unable to add incompatible type to number.").Throw();
        }
        return integer.ZERO;
    }
    
    @Override
    public number Push(object object) {
        if (object instanceof number number)
            return new integer(this.booleanValue() << number.booleanValue());
        new error("Unable to push incompatible type into number.").Throw();
        return this;
    }
    
    @Override
    public number Pull(object object) {
        if (object instanceof number number)
            return new integer(this.booleanValue() >> number.booleanValue());
        new error("Unable to pull incompatible type into number.").Throw();
        return this;
    }
    
    @Override
    public int booleanValue() {
        return 0;
    }
    
    @Override
    public integer Equals(object object) {
        if (this.booleanValue() == object.booleanValue()) return integer.ONE;
        return integer.ZERO;
    }
}
