package com.github.hexomod.replace.token;

public class Main {

    private static String VERSION = "@VERSION@";
    private static int INT = Integer.parseInt("@INT@");
    private static double DOUBLE = Double.parseDouble("@DOUBLE@");

    public static void main(String [] args) {
        System.out.println("VERSION = " + VERSION);
        System.out.println("INT = " + INT);
        System.out.println("DOUBLE = " + DOUBLE);
    }
}
