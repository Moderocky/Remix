package rmx;

public class string implements object {
    
    private String value = "";
    private boolean frozen;
    
    public string(String value) {
        this.value = value;
    }
    
    public string() {
    }
    
    public String value() {
        return value;
    }
    
    @Override
    public integer Frozen() {
        if (frozen) return integer.ONE;
        return integer.ZERO;
    }
    
    @Override
    public void Freeze() {
        this.frozen = true;
    }
    
    @Override
    public string Add(object object) {
        return new string(value + object);
    }
    
    @Override
    public object Push(object object) {
        if (frozen) return this.Add(object);
        this.value = value + object;
        return this;
    }
    
    @Override
    public integer Boolean() {
        if (!value.isEmpty()) return integer.ONE;
        return integer.ZERO;
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
    @Override
    public String toString() {
        return value;
    }
    
}
