package mx.kenzie.remix.builder;

import mx.kenzie.remix.meta.TypeStub;

import java.util.Arrays;

interface Builder {
    
    default TypeStub[] add(TypeStub[] stubs, TypeStub stub) {
        for (final TypeStub typeStub : stubs) {
            if (typeStub.equals(stub)) return stubs;
        }
        return this.addAny(stubs, stub);
    }
    
    default TypeStub[] addAny(TypeStub[] stubs, TypeStub stub) {
        final TypeStub[] array = this.grow(stubs);
        array[array.length - 1] = stub;
        return array;
    }
    
    default TypeStub[] grow(TypeStub[] stubs) {
        return Arrays.copyOf(stubs, stubs.length + 1);
    }
    
}
