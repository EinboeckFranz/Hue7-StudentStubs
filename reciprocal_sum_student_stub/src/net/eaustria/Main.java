package net.eaustria;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }

    public void run() {
        double[] randomDoubleArray = getARandomDoubleArray();
        long startTimeSeq = System.currentTimeMillis();
        double sequentialSum = ReciprocalArraySum.seqArraySum(randomDoubleArray);
        long endTimeSeq = System.currentTimeMillis();
        long startTimeReciprocal = System.currentTimeMillis();
        double reciprocalSum = ReciprocalArraySum.parManyTaskArraySum(randomDoubleArray, 100);
        long endTimeReciprocal = System.currentTimeMillis();

        System.out.println("Sequential: " + sequentialSum + " and took " + (endTimeSeq - startTimeSeq) + "ms.");
        System.out.println("Split in Tasks: " + reciprocalSum + " and took " + (endTimeReciprocal - startTimeReciprocal) + "ms.");
    }

    private double[] getARandomDoubleArray() {
        //MIN = 10.0
        //MAX = 20.0
        //ArraySize = 15000
        return Arrays.stream(new double[15000])
                .map(numb -> ((Math.random()*(20.0 - 10.0)+10.0) * 100) / 100)
                .toArray();
    }
}
