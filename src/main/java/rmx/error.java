package rmx;

public class error extends Throwable implements object {
    
    public final String message;
    
    public error(String message) {
        this.message = message;
    }
    
    public void Throw() {
        system.system().Error(this);
    }
    
    @Override
    public integer Frozen() {
        return integer.ONE;
    }
}
