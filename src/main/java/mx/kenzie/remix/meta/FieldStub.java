package mx.kenzie.remix.meta;

import java.lang.reflect.Field;

public record FieldStub(TypeStub owner, TypeStub type, String name) {
    
    public FieldStub(Field field) {
        this(TypeStub.of(field.getDeclaringClass()), TypeStub.of(field.getType()), field.getName());
    }
    
}
