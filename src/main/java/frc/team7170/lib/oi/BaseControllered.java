package frc.team7170.lib.oi;

import java.util.Objects;

abstract class BaseControllered implements Controllered {

    private final Controller controller;

    BaseControllered(Controller controller) {
        this.controller = Objects.requireNonNull(controller, "controller must be non-null");
    }

    @Override
    public Controller getController() {
        return controller;
    }
}
