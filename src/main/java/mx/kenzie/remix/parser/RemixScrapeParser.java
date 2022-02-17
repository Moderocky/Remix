package mx.kenzie.remix.parser;

import mx.kenzie.remix.compiler.Context;
import mx.kenzie.remix.lang.Element;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class RemixScrapeParser extends RemixParser implements Closeable {
    
    public RemixScrapeParser(InputStream stream, Context context) {
        super(stream, context);
    }
    
    public RemixScrapeParser(InputStream stream) {
        super(stream);
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
                this.closeElement();
                this.reader.skip();
                return;
            }
            case '.' -> this.mode = ParseMode.DOT_WORD;
            case '$' -> this.mode = ParseMode.DOLLAR_WORD;
            case '+', '-', '*', '/', '=', '!', '?', '&', '|' -> this.mode = ParseMode.SYMBOL;
            case '<', '>' -> this.mode = ParseMode.SYMBOL_2;
            case '"' -> this.mode = ParseMode.STRING;
        }
        this.context.clearErrors();
        final String part = mode.read(reader);
        if (!reader.isEmpty()) {
            this.reader.readWhitespace();
            this.context.setUpcoming(reader.upcoming());
        }
        final Element element = context.findElement(part);
        if (element == null) {
            context.emptyBuffer();
            return;
        }
        element.write(context, part);
        context.emptyBuffer();
        if (element.hasBody()) this.current(element, part);
        this.mode = element.nextMode(mode);
    }
    
    protected void openElement() {
        try {
            final Element element = current.get(0);
            this.context.open(element, stubs.get(0));
        } catch (Throwable ignored) {}
    }
    
    protected void closeElement() {
        try {
            boolean result;
            do {
                final Element element = this.current.remove(0);
                result = this.context.close(element, stubs.remove(0));
            } while (!result && !this.current.isEmpty());
        } catch (Throwable ignored) {}
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
