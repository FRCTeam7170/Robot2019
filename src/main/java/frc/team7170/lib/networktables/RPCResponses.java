package frc.team7170.lib.networktables;


public final class RPCResponses {

    public static final byte[] EMPTY = {};
    public static final byte[] YES = "_YES".getBytes();
    public static final byte[] NO = "_NO".getBytes();
    public static final byte[] SUCCESS = "_SUCCESS".getBytes();
    public static final byte[] FAIL = "_FAIL".getBytes();
    public static final byte[] ERROR = "_ERROR".getBytes();

    private RPCResponses() {}
}
