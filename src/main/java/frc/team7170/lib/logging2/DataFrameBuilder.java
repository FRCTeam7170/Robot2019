package frc.team7170.lib.logging2;

public interface DataFrameBuilder {

    void addFrame(DataFrame dataFrame);

    DataFrame newPeriodicFrame(String name, int pollPeriodMs);

    DataFrame newStaticFrame(String name);

    DataFrame newConsolidatedFrame(String name, DataFrame parent);

    DataFrame getFrame(String name);

    boolean removeFrame(String name);
}
