package ru.kpfu.itis.kirillakhmetov.work;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        BTree tree = new BTree();
        long start, end;


        // Считаем добавление
        int[] arr = generateRandomIntArray(10000, null);
        long[] timesAddition = new long[10000];
        long[] countOperationAddition = new long[10000];
        for (int i = 0; i < arr.length; i++) {
            start = System.nanoTime();
            tree.add(arr[i]);
            end = System.nanoTime();
            timesAddition[i] = end - start;
            countOperationAddition[i] = tree.getCounterOperations();
            tree.setCounterOperations(0);
        }
        System.out.println("Среднее время для вставки: " + getAverageValue(timesAddition) + " нс.");
        System.out.println("Среднее количество операций для вставки: " + getAverageValue(countOperationAddition));

        // Считаем поиск
        int[] elemsForFind = randomSelectionElems(arr, 100);
        long[] timesFind = new long[100];
        long[] countOperationFind = new long[100];
        for (int i = 0; i < elemsForFind.length; i++) {
            start = System.nanoTime();
            tree.find(elemsForFind[i]);
            end = System.nanoTime();
            timesFind[i] = end - start;
            countOperationFind[i] = tree.getCounterOperations();
            tree.setCounterOperations(0);
        }
        System.out.println("Среднее время для поиска: " + getAverageValue(timesFind) + " нс.");
        System.out.println("Среднее количество операций для поиска: " + getAverageValue(countOperationFind));

        // Считаем удаление
        int[] elemsForDelete = randomSelectionElems(arr, 1000);
        long[] timesDelete = new long[1000];
        long[] countOperationDelete = new long[1000];
        for (int i = 0; i < elemsForDelete.length; i++) {
            start = System.nanoTime();
            tree.remove(elemsForDelete[i]);
            end = System.nanoTime();
            timesDelete[i] = end - start;
            countOperationDelete[i] = tree.getCounterOperations();
            tree.setCounterOperations(0);
        }

        System.out.println("Среднее время для удаления: " + getAverageValue(timesDelete) + " нс.");
        System.out.println("Среднее количество операций для удаления: " + getAverageValue(countOperationDelete));
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

    private static long getAverageValue(long[] arr) {
        long sum = 0;

        for (long el : arr) {
            sum += el;
        }

        return sum / arr.length;
    }
}
