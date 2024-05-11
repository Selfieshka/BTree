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
        int[] arr = generateRandomIntArray(10000, null);
        int[] arr2 = randomSelectionElems(arr, 100);
        int[] arr3 = randomSelectionElems(arr, 1000);
    }

    private static int[] generateRandomIntArray(int size, Integer upperBound) {
        Set<Integer> generatedNumbers = new HashSet<>();
        Random random = new Random();
        if (upperBound == null) {
            while (generatedNumbers.size() < size) {
                generatedNumbers.add(random.nextInt());
            }
        } else {
            while (generatedNumbers.size() < size) {
                generatedNumbers.add(random.nextInt(upperBound));
            }
        }

        return generatedNumbers.stream().mapToInt(Integer::intValue).toArray();
    }

    private static int[] randomSelectionElems(int[] arr, int countElems) {
        int[] indexes = generateRandomIntArray(countElems, arr.length);
        int[] randomElems = new int[countElems];

        for (int i = 0; i < countElems; i++) {
            randomElems[i] = arr[indexes[i]];
        }

        return randomElems;
    }
}
