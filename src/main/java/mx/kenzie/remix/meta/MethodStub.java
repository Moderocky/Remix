package mx.kenzie.remix.meta;

import java.lang.reflect.Method;

public record MethodStub(TypeStub owner, TypeStub result, String name, TypeStub... parameters) {
    
    public MethodStub(Method method) {
        this(TypeStub.of(method.getDeclaringClass()), TypeStub.of(method.getReturnType()), method.getName(), TypeStub.of(method.getParameterTypes()));
    }
    
}
