package mx.kenzie.remix.builder;

import mx.kenzie.remix.meta.FunctionStub;
import mx.kenzie.remix.meta.TypeStub;
import org.objectweb.asm.*;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TypeBuilder implements Builder {
    
    public static final FunctionStub
        EQUALS = new FunctionStub(0, TypeStub.OBJECT, TypeStub.INTEGER, "Equals", TypeStub.OBJECT),
        ADD = new FunctionStub(0, TypeStub.OBJECT, TypeStub.OBJECT, "Add", TypeStub.OBJECT),
        SUB = new FunctionStub(0, TypeStub.OBJECT, TypeStub.OBJECT, "Sub", TypeStub.OBJECT),
        MUL = new FunctionStub(0, TypeStub.OBJECT, TypeStub.OBJECT, "Mul", TypeStub.OBJECT),
        DIV = new FunctionStub(0, TypeStub.OBJECT, TypeStub.OBJECT, "Div", TypeStub.OBJECT),
        PUSH = new FunctionStub(0, TypeStub.OBJECT, TypeStub.OBJECT, "Push", TypeStub.OBJECT),
        PULL = new FunctionStub(0, TypeStub.OBJECT, TypeStub.OBJECT, "Pull", TypeStub.OBJECT),
        AND = new FunctionStub(0, TypeStub.OBJECT, TypeStub.OBJECT, "And", TypeStub.OBJECT),
        OR = new FunctionStub(0, TypeStub.OBJECT, TypeStub.OBJECT, "Or", TypeStub.OBJECT),
        BOOL = new FunctionStub(0, TypeStub.OBJECT, TypeStub.INTEGER, "Or"),
        NEG = new FunctionStub(0, TypeStub.OBJECT, TypeStub.INTEGER, "Neg");
    protected final List<FunctionBuilder> functions = new ArrayList<>();
    protected final List<FieldBuilder> fields = new ArrayList<>();
    protected final TypeStub stub;
    private List<FunctionBuilder> operators;
    
    public TypeBuilder(String name) {
        this.stub = TypeStub.of(name, TypeStub.of(Object.class), TypeStub.of(rmx.object.class));
    }
    
    public TypeStub getType() {
        return stub;
    }
    
    public void extend(TypeStub stub) {
        this.stub.setSuperclass(stub);
    }
    
    public void implement(TypeStub stub) {
        final TypeStub[] interfaces = this.add(this.stub.interfaces(), stub);
        this.stub.setInterfaces(interfaces);
    }
    
    public FieldBuilder startField(int modifiers, String name, TypeStub type) {
        final FieldBuilder builder = new FieldBuilder(modifiers, stub, name, type);
        this.fields.add(builder);
        this.stub.addField(builder.stub);
        return builder;
    }
    
    public ClassWriter writer() {
        final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        writer.visit(Opcodes.V17, stub.modifiers(), stub.internal(), null, stub.superName(), stub.interfaceNames());
        for (final FieldBuilder field : fields) {
            final FieldVisitor visitor = writer.visitField(field.modifiers, field.stub.name(), field.stub.type().toASM()
                .getDescriptor(), null, null);
            field.accept(visitor);
        }
        for (final FunctionBuilder function : functions) {
            final MethodVisitor visitor = writer.visitMethod(function.stub.modifiers(), function.stub.name(), function.stub.descriptor(), null, new String[0]);
            function.accept(visitor);
        }
        return writer;
    }
    
    public boolean hasFunction(String name, TypeStub... parameters) {
        for (final FunctionBuilder function : functions) {
            if (function.stub.name().equals(name) && Arrays.equals(parameters, function.stub.parameters())) return true;
        }
        return false;
    }
    
    public void bakeOperators() {
        final List<FunctionBuilder> list = new ArrayList<>(functions);
        list.removeIf(function -> !function.operator);
        this.operators = list;
        this.bake(EQUALS);
        this.bake(ADD);
        this.bake(SUB);
        this.bake(MUL);
        this.bake(DIV);
        this.bake(PUSH);
        this.bake(PULL);
        this.bake(AND);
        this.bake(OR);
        this.bake(BOOL);
        this.bake(NEG);
    }
    
    private void bake(FunctionStub archetype) {
        if (this.hasFunction(archetype)) return;
        final FunctionBuilder builder = this.startFunction(archetype.name(), Modifier.PUBLIC | Modifier.SYNCHRONIZED);
        builder.setReturnType(archetype.result());
        final boolean arguments = archetype.parameters().length > 0;
        if (arguments) {
            for (final TypeStub parameter : archetype.parameters()) builder.addParameter(parameter);
            builder.write(visitor -> {
                visitor.visitVarInsn(25, 1);
//                visitor.visitMethodInsn(182, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false);
//                visitor.visitVarInsn(58, 2);
            });
            final List<FunctionBuilder> list = new ArrayList<>(operators);
            list.removeIf(function -> !function.getStub().name().equals(archetype.name()));
            list.removeIf(function -> function.getStub().parameters().length < 1);
            for (final FunctionBuilder function : list) {
                final Label label = new Label();
                final TypeStub stub = function.stub.parameters()[0];
//                builder.write(function.stub.parameters()[0].typeLiteral());
                builder.write(visitor -> {
                    visitor.visitVarInsn(25, 1);
                    
                    visitor.visitTypeInsn(193, stub.internal());
                    visitor.visitJumpInsn(153, label);
                    visitor.visitVarInsn(25, 1);
                    visitor.visitTypeInsn(192, stub.internal());
                    visitor.visitVarInsn(25, 0);
                    visitor.visitInsn(95);
                });
                builder.write(function.stub.write());
                if (!function.stub.result().equals(archetype.result()))
                    builder.write(visitor -> visitor.visitTypeInsn(192, archetype.result().internal()));
                builder.write(visitor -> {
                    visitor.visitInsn(176);
                    visitor.visitLabel(label);
                });
            }
        }
        final TypeStub parent;
        if (this.stub.superclass() != null && this.stub.superclass().canCastTo(TypeStub.OBJECT))
            parent = this.stub.superclass();
        else parent = TypeStub.OBJECT;
        builder.write(visitor -> {
            visitor.visitVarInsn(25, 0);
            if (arguments) visitor.visitVarInsn(25, 1);
        });
        builder.write(visitor -> visitor.visitMethodInsn(183, parent.internal(), archetype.name(), builder.stub.descriptor(), parent.isInterface()));
        builder.write(visitor -> visitor.visitInsn(176));
    }
    
    public boolean hasFunction(FunctionStub stub) {
        for (final FunctionBuilder function : functions) {
            if (function.stub.equals(stub)) return true;
        }
        return false;
    }
    
    public FunctionBuilder startFunction(String name, int modifiers) {
        final FunctionBuilder builder = new FunctionBuilder(modifiers, stub, name);
        this.functions.add(builder);
        this.stub.addMethod(builder.stub);
        return builder;
    }
}
