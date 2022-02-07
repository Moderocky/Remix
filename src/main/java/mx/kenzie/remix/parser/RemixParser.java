package mx.kenzie.remix.parser;

import mx.kenzie.jupiter.reader.StreamReader;
import mx.kenzie.jupiter.stream.InputStreamController;
import mx.kenzie.jupiter.stream.Stream;
import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RemixParser implements Closeable {
    
    public static final char DOWN = ':', UP = ';';
    
    protected final InputStreamController stream;
    protected final StreamReader reader;
    protected volatile boolean parse;
    protected volatile ParseMode mode;
    protected final List<Element> current;
    protected volatile Context context;
    
    public RemixParser(InputStream stream) {
        this.stream = Stream.controller(stream);
        this.reader = new StreamReader(stream);
        this.current = new ArrayList<>();
    }
    
    public synchronized void parse() throws IOException {
        this.mode = ParseMode.WORD;
        while (parse && !reader.isEmpty()) {
            this.step();
        }
    }
    
    public void step() throws IOException {
        this.reader.readWhitespace();
        if (reader.isEmpty()) return;
        switch (reader.upcoming()) {
            case ':' -> this.current().open(context);
            case ';' -> this.current().close(context);
        }
        final String part = mode.read(reader);
        final Element element = context.findElement(part);
        element.write(context, part);
        if (element.hasBody()) this.current(element);
    }
    
    @Override
    public void close() throws IOException {
        this.stream.close();
        this.reader.close();
    }
    
    protected Element current() {
        return current.get(0);
    }
    
    protected void current(Element element) {
        this.current.add(0, element);
    }
    
    protected boolean isWhitespace(char c) {
        return c == ' ';
    }
    
}
