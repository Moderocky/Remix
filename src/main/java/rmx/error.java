package rmx;

public class error extends Throwable implements object {
    
    public final string message;
    
    public error(String message) {
        super(message);
        this.message = new string(message);
    }
    
    public error(string message) {
        super(message.value());
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
