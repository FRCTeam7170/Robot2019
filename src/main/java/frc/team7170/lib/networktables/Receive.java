package frc.team7170.lib.networktables;

import edu.wpi.first.networktables.EntryListenerFlags;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Receive {
    String value() default "";
    int flags() default EntryListenerFlags.kUpdate;
}
