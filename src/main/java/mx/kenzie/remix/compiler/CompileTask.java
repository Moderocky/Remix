package mx.kenzie.remix.compiler;

@FunctionalInterface
public interface CompileTask {
    
    void run(Context context);
    
}
