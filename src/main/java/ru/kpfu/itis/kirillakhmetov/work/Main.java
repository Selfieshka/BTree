package ru.kpfu.itis.kirillakhmetov.work;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Tree tree = new BTree();
//        int[] arr = {30, 70, 8, 25, 40, 50, 76, 88, 1, 3, 7, 15, 21, 23, 26, 28, 35, 38, 42, 49, 56, 67, 71, 73, 75, 77, 85, 89, 97};
        int[] arr = generateRandomIntArray();

    }

    private static int[] generateRandomIntArray() {
        final int SIZE = 10000;
        Set<Integer> generatedNumbers = new HashSet<>();
        Random random = new Random();

        while (generatedNumbers.size() < SIZE) {
            generatedNumbers.add(random.nextInt());
        }

        return generatedNumbers.stream().mapToInt(Integer::intValue).toArray();
    }
}
