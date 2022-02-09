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
    protected final List<Element> current;
    protected final List<String> stubs;
    protected volatile boolean parse;
    protected volatile ParseMode mode;
    protected volatile Context context;
    
    public RemixParser(InputStream stream, Context context) {
        this(stream);
        this.context = context;
    }
    
    public RemixParser(InputStream stream) {
        this.stream = Stream.controller(stream);
        this.reader = new StreamReader(stream);
        this.current = new ArrayList<>();
        this.stubs = new ArrayList<>();
    }
    
    public synchronized void parse() throws IOException {
        this.parse = true;
        this.mode = ParseMode.WORD;
        while (parse && !reader.isEmpty()) {
            this.step();
        }
    }
    
    public synchronized void step() throws IOException {
        this.reader.readWhitespace();
        if (reader.isEmpty()) return;
        switch (reader.upcoming()) {
            case DOWN -> {
                this.openElement();
                this.reader.skip();
                return;
            }
            case UP -> {
                this.closeElement(); // todo needs to change
                this.reader.skip();
                return;
            }
            case '.' -> this.mode = ParseMode.DOT_WORD;
            case '"' -> this.mode = ParseMode.STRING;
        }
        final String part = mode.read(reader);
        if (!reader.isEmpty()) {
            this.reader.readWhitespace();
            this.context.setUpcoming(reader.upcoming());
        }
        final Element element = context.findElement(part);
        assert element != null : part + ": evaluated to null";
        element.write(context, part);
        if (element.hasBody()) this.current(element, part);
        this.mode = element.nextMode(mode);
    }
    
    protected void openElement() {
        final Element element = current.get(0);
        element.open(context, stubs.get(0));
    }
    
    protected void closeElement() {
        final Element element = this.current.remove(0);
        element.close(context, stubs.remove(0));
    }
    
    protected void current(Element element, String stub) {
        this.current.add(0, element);
        this.stubs.add(0, stub);
    }
    
    @Override
    public void close() throws IOException {
        this.stream.close();
        this.reader.close();
    }
    
}
