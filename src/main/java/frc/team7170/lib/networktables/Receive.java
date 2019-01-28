package frc.team7170.lib.networktables;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Repeatable(Receives.class)
public @interface Receive {
    String value();
}
