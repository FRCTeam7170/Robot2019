package frc.team7170;

public class Test {

    public static void main(String... args) {
        tester(null);
    }

    public static void tester(Object... args) {
        for (Object o : args) {
            System.out.println(o);
        }
    }
}
