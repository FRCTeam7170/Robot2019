package frc.team7170.lib;


public class AveragePrinter {

    private int count = 0;
    private final int numIters;
    private double sum = 0;
    private final String printPrefix;

    public AveragePrinter(int numIters, String printPrefix) {
        this.numIters = numIters;
        this.printPrefix = printPrefix;
    }

    public AveragePrinter(int numIters) {
        this(numIters, "");
    }

    public boolean feed(double data) {
        sum += data;
        count++;
        if (count == numIters) {
            System.out.println(printPrefix + sum / (double) numIters);
            sum = 0;
            count = 0;
            return true;
        }
        return false;
    }
}
