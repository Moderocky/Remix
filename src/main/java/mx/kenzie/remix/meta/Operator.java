package mx.kenzie.remix.meta;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.List;

@Retention(RetentionPolicy.SOURCE)
public @interface Operator {
    List<String> OPERATORS = Arrays.asList("Add", "Sub", "Mul", "Div", "Push", "Pull", "Bool", "Neg", "Equals", "And", "Or");
    
    String value();
    
}
