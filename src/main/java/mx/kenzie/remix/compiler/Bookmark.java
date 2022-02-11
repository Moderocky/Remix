package mx.kenzie.remix.compiler;

import mx.kenzie.remix.lang.Element;
import org.objectweb.asm.Label;

public record Bookmark(Element element, Label start, Label end) {
    
    public Bookmark(Element element) {
        this(element, new Label(), new Label());
    }
    
}
