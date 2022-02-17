package mx.kenzie.remix.app;

import mx.kenzie.remix.compiler.BuildTask;
import mx.kenzie.remix.compiler.CompileContext;
import mx.kenzie.remix.lang.code.*;
import mx.kenzie.remix.lang.field.KeywordTransient;
import mx.kenzie.remix.lang.field.NameField;
import mx.kenzie.remix.lang.field.TypeField;
import mx.kenzie.remix.lang.function.FunctionName;
import mx.kenzie.remix.lang.function.FunctionParameter;
import mx.kenzie.remix.lang.function.FunctionParameterName;
import mx.kenzie.remix.lang.function.KeywordFunction;
import mx.kenzie.remix.lang.literal.LiteralBoolean;
import mx.kenzie.remix.lang.literal.LiteralDecimal;
import mx.kenzie.remix.lang.literal.LiteralInteger;
import mx.kenzie.remix.lang.literal.LiteralString;
import mx.kenzie.remix.lang.operator.KeywordOperator;
import mx.kenzie.remix.lang.operator.OperatorName;
import mx.kenzie.remix.lang.operator.OperatorParameter;
import mx.kenzie.remix.lang.operator.OperatorParameterName;
import mx.kenzie.remix.lang.symbol.*;
import mx.kenzie.remix.lang.type.KeywordExtends;
import mx.kenzie.remix.lang.type.KeywordType;
import mx.kenzie.remix.lang.type.TypeExtends;
import mx.kenzie.remix.lang.type.TypeName;
import mx.kenzie.remix.meta.TypeStub;

import java.io.File;
import java.io.IOException;

public class RemixApp {
    
    public static void main(String[] args) {
    
    }
    
    public static void compile(File output, File... files) throws IOException {
        final BuildTask task = new BuildTask(RemixApp::defaultContext, files);
        task.search();
        task.parseCompile(output);
    }
    
    public static CompileContext defaultContext() {
        return new CompileContext(
            new TypeStub[]{
                TypeStub.of(rmx.object.class),
                TypeStub.of(rmx.system.class),
                TypeStub.of(rmx.string.class),
                TypeStub.of(rmx.number.class),
                TypeStub.of(rmx.integer.class),
                TypeStub.of(rmx.decimal.class),
                TypeStub.of(rmx.type.class),
                TypeStub.of(rmx.pointer.class),
            },
            new KeywordType(),
            new TypeName(),
            new KeywordExtends(),
            new TypeExtends(),
            
            new KeywordOperator(),
            new OperatorName(),
            new OperatorParameter(),
            new OperatorParameterName(),
            
            new KeywordFunction(),
            new FunctionName(),
            new FunctionParameter(),
            new FunctionParameterName(),
            
            new KeywordTransient(),
            new TypeField(),
            new NameField(),
            
            new SymbolAdd(),
            new SymbolSubtract(),
            new SymbolMultiply(),
            new SymbolDivide(),
            new SymbolPush(),
            new SymbolPull(),
            new SymbolEquals(),
            new SymbolAnd(),
            new SymbolOr(),
            new SymbolBool(),
            new SymbolNeg(),
            
            new KeywordIf(),
            new KeywordWhile(),
            new KeywordBreak(),
            
            new KeywordDuplicate(),
            new KeywordPop(),
            new KeywordSwap(),
            new KeywordExit(),
            new KeywordAllocate(),
            new TypeAllocate(),
            new KeywordNew(),
            new TypeNew(),
            new KeywordCast(),
            new TypeCast(),
            new KeywordInstance(),
            new TypeInstance(),
            
            new ConstantThis(),
            new ConstantSystem(),
            new ConstantTop(),
            
            new FieldCall(),
            new FunctionCall(),
            
            new LiteralString(),
            new LiteralInteger(),
            new LiteralDecimal(),
            new LiteralBoolean(),
            
            new VariableUse(),
            new VariableType(),
            new VariableName()
        );
    }
    
}
