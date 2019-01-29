package frc.team7170.lib.networktables;

import edu.wpi.first.networktables.EntryListenerFlags;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CommField {
    String value() default "";
    int pollRateMs() default TransmitFrequency.MODERATE;
    int flags() default EntryListenerFlags.kUpdate;
    boolean transmit() default true;
    boolean receive() default true;
}
