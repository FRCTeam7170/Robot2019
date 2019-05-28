package frc.team7170;

import java.math.BigInteger;

public class Test {

    private static BigInteger n = new BigInteger("100010101011100010101101110101011101110101100010101011100010011111010101011101110101100010001000100010107");

    public static void main(String... args) {
        System.out.println(n.isProbablePrime(20));
    }
}
