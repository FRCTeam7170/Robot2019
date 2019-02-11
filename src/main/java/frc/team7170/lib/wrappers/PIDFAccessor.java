package frc.team7170.lib.wrappers;

// TODO: merge with ProfileAccessor?
public interface PIDFAccessor {

    void setP(double P);

    double getP();

    void setI(double I);

    double getI();

    void setD(double D);

    double getD();

    void setF(double F);

    double getF();

    default void setPIDF(double P, double I, double D, double F) {
        setP(P);
        setI(I);
        setD(D);
        setF(F);
    }

    default void setPID(double P, double I, double D) {
        setPIDF(P, I, D, 0.0);
    }
}
