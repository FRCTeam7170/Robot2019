package frc.team7170.lib.profiling;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


// TODO: REMOVE
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Profile {

    int value() default -1;
}
