package mx.kenzie.remix.meta;

import org.objectweb.asm.MethodVisitor;

import java.util.function.Consumer;

public record BufferEntry(Consumer<MethodVisitor> consumer, int pop, TypeStub push) {
}
