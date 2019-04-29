package frc.team7170.lib;

import java.util.ArrayList;

public class MovingAverage {

    private final ArrayList<Double> numbers;
    private final int frameSize;

    public MovingAverage(int frameSize) {
        numbers = new ArrayList<>(frameSize);
        this.frameSize = frameSize;
    }

    public void add(double number) {
        if (isFull()) {
            numbers.remove(0);
        }
        numbers.add(number);
    }

    public void addAll(double... numbers) {
        for (double number : numbers) {
            add(number);
        }
    }

    public double getAverage() {
        double total = 0.0;

        for (double number : numbers) {
            total += number;
        }

        return total / numbers.size();
    }

    public int getSize() {
        return numbers.size();
    }

    public boolean isFull() {
        return numbers.size() == frameSize;
    }

    public void clear() {
        numbers.clear();
    }
}
