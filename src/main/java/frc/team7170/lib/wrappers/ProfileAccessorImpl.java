package frc.team7170.lib.wrappers;

class ProfileAccessorImpl implements ProfileAccessor {

    private double P, I, D, F, IZone, minimumOutput, maximumOutput, tolerance;

    @Override
    public void setIZone(double IZone) {
        this.IZone = IZone;
    }

    @Override
    public double getIZone() {
        return IZone;
    }

    @Override
    public void setMinimumOutput(double minimumOutput) {
        this.minimumOutput = minimumOutput;
    }

    @Override
    public double getMinimumOutput() {
        return minimumOutput;
    }

    @Override
    public void setMaximumOutput(double maximumOutput) {
        this.maximumOutput = maximumOutput;
    }

    @Override
    public double getMaximumOutput() {
        return maximumOutput;
    }

    @Override
    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    @Override
    public double getTolerance() {
        return tolerance;
    }

    @Override
    public void setP(double P) {
        this.P = P;
    }

    @Override
    public double getP() {
        return P;
    }

    @Override
    public void setI(double I) {
        this.I = I;
    }

    @Override
    public double getI() {
        return I;
    }

    @Override
    public void setD(double D) {
        this.D = D;
    }

    @Override
    public double getD() {
        return D;
    }

    @Override
    public void setF(double F) {
        this.F = F;
    }

    @Override
    public double getF() {
        return F;
    }
}