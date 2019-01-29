package frc.team7170.lib.networktables;

import edu.wpi.first.networktables.EntryListenerFlags;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Repeatable(Receives.class)
public @interface Receive {
    String value() default "";
    int flags() default EntryListenerFlags.kUpdate;
}
