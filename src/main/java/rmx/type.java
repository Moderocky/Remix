package rmx;

public class type implements object {
    
    private final Class<?> type;
    
    public type(Class<?> type) {
        this.type = type;
    }
    
    public type() {
        this.type = rmx.object.class;
    }
    
    @Override
    public boolean Frozen() {
        return true;
    }
    
    public Class<?> getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return type.getSimpleName();
    }
    
}
