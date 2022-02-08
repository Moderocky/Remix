package rmx;

public class decimal extends number {
    
    final double value;
    
    public decimal(double value) {
        this.value = value;
    }
    
    public decimal(number number, double appendix) {
        final double source;
        if (number instanceof decimal decimal)
            source = decimal.value;
        else source = number.booleanValue();
        if (source < 0) this.value = source - appendix;
        else this.value = source + appendix;
    }
    
    public decimal() {
        this.value = 0;
    }
    
    public double value() {
        return value;
    }
    
    public decimal Add(object to) {
        if (to instanceof decimal number) {
            return new decimal(value + number.value);
        } else if (to instanceof integer number) {
            return new decimal(value + number.booleanValue());
        } else if (to instanceof number number) {
            return new decimal(value + number.booleanValue());
        } else {
            new error("Unable to add incompatible type to number.").Throw();
        }
        return new decimal();
    }
    
    @Override
    public int booleanValue() {
        return (int) value;
    }
    
    @Override
    public integer Equals(object object) {
        if (object instanceof decimal number) {
            return bool(number.value == this.value);
        } else if (object instanceof integer number) {
            return bool((double) number.value == this.value);
        } else if (object instanceof number number) {
            return bool((double) number.booleanValue() == this.value);
        } else {
            return integer.ZERO;
        }
    }
    
    @Override
    public String toString() {
        return value + "";
    }
    
}
