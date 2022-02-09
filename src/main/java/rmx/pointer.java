package rmx;

public class pointer extends number {
    
    private final long value;
    
    public pointer() {
        this.value = 0;
    }
    
    public pointer(long value) {
        this.value = value;
    }
    
    public long value() {
        return value;
    }
    
    @Override
    public int booleanValue() {
        return (int) value;
    }
    
}
