package ru.kpfu.itis.kirillakhmetov.work;

public class Node {
    private int degree;
    private final int[] keys;
    private final Node[] children;
    private boolean leaf;
    public int counterOperations = 0;

    public Node(int t) {
        this.keys = new int[2 * t - 1];
        this.children = new Node[2 * t];
        this.leaf = true;
        this.degree = 0;
    }

    public int find(int k) {
        for (int i = 0; i < degree; i++) {
            if (keys[i] == k) {
                return i;
            }
        }
        return -1;
    }

    public void insertKey(int key) {
        if (counterOperations != 0) counterOperations = 0;
        int i = 0;
        // Циклический сдвиг вправо элементов, больших key
        for (i = degree - 1; i >= 0 && key < keys[i]; i--) {
            keys[i + 1] = keys[i];
            counterOperations++;
        }

        keys[i + 1] = key;
        degree++;
        counterOperations += 3;
    }

    public void removeKey(int key) {
        int i;
        for (i = 0; i <= degree && keys[i] != key; i++) {
        }

        // Циклический сдвиг влево элементов, больших key
        for (int j = i; i < degree - 1; i++) {
            keys[i] = keys[i + 1];
        }
        keys[i] = 0;
        degree--;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public int getDegree() {
        return degree;
    }

    public int[] getKeys() {
        return keys;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public Node[] getChildren() {
        return children;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }
}
