package ru.kpfu.itis.kirillakhmetov.work;

public class Main {
    public static void main(String[] args) {
        final int ORDER_TREE = 2;
        Btree btree = new Btree(ORDER_TREE);
        int[] arr = new int[] {30, 70, 8, 25, 40, 50, 76, 88, 1, 3, 7, 15, 21, 23, 26, 28, 35, 38, 42, 49, 56, 67, 71, 73, 75, 77, 85, 89, 97};

        for (int el : arr) {
            btree.add(el);
        }
        btree.show();
    }
}