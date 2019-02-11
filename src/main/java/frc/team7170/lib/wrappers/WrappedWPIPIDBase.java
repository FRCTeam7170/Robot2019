package frc.team7170.lib.wrappers;

import edu.wpi.first.wpilibj.PIDBase;

class WrappedWPIPIDBase extends AbstractPIDFController {

    private final PIDBase pidBase;

    WrappedWPIPIDBase(PIDBase pidBase) {
        this.pidBase = pidBase;
    }

    @Override
    ProfileAccessor newProfileAccessor(int slot) {
        return new ProfileAccessorImpl() {
            @Override
            public void setIZone(double IZone) {
                throw new UnsupportedOperationException();
            }

            @Override
            public double getIZone() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    void loadProfileAccessor(ProfileAccessor profileAccessor) {
        pidBase.setP(profileAccessor.getP());
        pidBase.setI(profileAccessor.getI());
        pidBase.setD(profileAccessor.getD());
        pidBase.setF(profileAccessor.getF());
        pidBase.setOutputRange(profileAccessor.getMinimumOutput(), profileAccessor.getMaximumOutput());
        pidBase.setAbsoluteTolerance(profileAccessor.getTolerance());
    }

    @Override
    void setSetpointRaw(MotorMode motorMode, double setpoint) {
        // PIDBase.setPIDSourceType and PIDBase.getPIDSourceType are package-private by mistake (issue submitted on GH).
        if (motorMode != MotorMode.POSITION) {
            throw new UnsupportedOperationException("PIDBase is bugged :/");
        }
        pidBase.setSetpoint(setpoint);
    }

    @Override
    double getErrorRaw() {
        return pidBase.getError();
    }
}
