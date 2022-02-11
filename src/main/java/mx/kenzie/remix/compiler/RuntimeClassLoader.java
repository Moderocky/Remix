package mx.kenzie.remix.compiler;

import mx.kenzie.remix.meta.TypeStub;

public class RuntimeClassLoader extends ClassLoader {
    public Class<?> load(TypeStub stub, byte[] bytes) {
        return this.defineClass(stub.getTypeName(), bytes, 0, bytes.length);
    }
}
