package net.eaustria.webcrawler;

import java.util.concurrent.TimeUnit;

public class STATIC {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";

    static void printStatistics(int size, long startTime) {
        System.out.println("It took " + TimeUnit.SECONDS.convert((System.nanoTime()-startTime), TimeUnit.NANOSECONDS) + "s to find " + size + " Links");
    }

    static void printErrorMSG(String msg) {
        System.out.println(ANSI_RED + msg + ANSI_RESET);
    }
}
