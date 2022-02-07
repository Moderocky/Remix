package mx.kenzie.remix.builder;

import mx.kenzie.remix.meta.TypeStub;

import java.util.Arrays;

interface Builder {
    
    default TypeStub[] addAny(TypeStub[] stubs, TypeStub stub) {
        final TypeStub[] array = Arrays.copyOf(stubs, stubs.length + 1);
        array[array.length - 1] = stub;
        return array;
    }
    
    default TypeStub[] add(TypeStub[] stubs, TypeStub stub) {
        Arrays.sort(stubs);
        if (Arrays.binarySearch(stubs, stub) > -1) return stubs;
        final TypeStub[] array = Arrays.copyOf(stubs, stubs.length + 1);
        array[array.length - 1] = stub;
        return array;
    }
    
    default TypeStub[] grow(TypeStub[] stubs) {
        return Arrays.copyOf(stubs, stubs.length + 1);
    }
    
}
