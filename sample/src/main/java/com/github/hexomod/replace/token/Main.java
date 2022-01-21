package com.github.hexomod.replace.token;

import java.io.*;

public class Main {

    private static String VERSION = "@VERSION@";
    private static int INT = Integer.parseInt("@INT@");
    private static double DOUBLE = Double.parseDouble("@DOUBLE@");

    public static void main(String[] args) {

        File file = new File(
                Main.class.getClassLoader().getResource("res.yml").getFile()
        );

        try {
            FileReader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("VERSION = " + VERSION);
        System.out.println("INT = " + INT);
        System.out.println("DOUBLE = " + DOUBLE);

        System.out.println(Greeting.getGreeting());
    }
}
