package rmx;

import mx.kenzie.remix.meta.Operator;

import java.util.ArrayList;

public class array implements object {
    
    private final ArrayList<object> list = new ArrayList<>();
    
    public synchronized object Get(number key) {
        if (list.size() > key.booleanValue())
            return list.get(key.booleanValue());
        return object.INSTANCE;
    }
    
    public integer Size() {
        if (list.isEmpty()) return integer.ZERO;
        return new integer(list.size());
    }
    
    public integer IsEmpty() {
        if (list.isEmpty()) return integer.ZERO;
        return integer.ONE;
    }
    
    public synchronized void Clear() {
        this.list.clear();
    }
    
    @Override
    @Operator("+")
    public array Add(object object) {
        if (object instanceof array array) return this.Add(array);
        return (array) object.super.Add(object);
    }
    
    @Override
    @Operator("-")
    public array Sub(object object) {
        if (object instanceof array array) return this.Sub(array);
        return (array) object.super.Sub(object);
    }
    
    @Override
    @Operator("<<")
    public synchronized array Push(object object) {
        this.list.add(object);
        return this;
    }
    
    @Override
    @Operator(">>")
    public synchronized array Pull(object object) {
        if (object instanceof integer integer)
            return this.Pull(integer);
        this.list.remove(object);
        return this;
    }
    
    @Override
    public int booleanValue() {
        return list.size();
    }
    
    @Override
    @Operator("&")
    public array And(object object) {
        if (object instanceof array array) return this.And(array);
        return (array) object.super.And(object);
    }
    
    @Override
    @Operator("|")
    public array Or(object object) {
        if (object instanceof array array) return this.Add(array);
        return (array) object.super.Or(object);
    }
    
    public array And(array object) {
        final array array = new array();
        array.list.addAll(this.list);
        array.list.removeIf(thing -> !object.list.contains(thing));
        return array;
    }
    
    @Operator(">>")
    public synchronized array Pull(integer object) {
        this.list.remove(object.booleanValue());
        return this;
    }
    
    public array Sub(array object) {
        final array array = new array();
        array.list.addAll(this.list);
        array.list.removeAll(object.list);
        return array;
    }
    
    public array Add(array object) {
        final array array = new array();
        array.list.addAll(this.list);
        array.list.addAll(object.list);
        return array;
    }
    
}
