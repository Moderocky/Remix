package mx.kenzie.remix.compiler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TrackerQueue {
    
    protected final List<AtomicInteger> list = new ArrayList<>();
    
    public void open() {
        list.add(0, new AtomicInteger());
    }
    
    public int close() {
        return list.remove(0).get();
    }
    
    public void count(int i) {
        for (final AtomicInteger integer : list) integer.getAndAdd(i);
    }
    
}
