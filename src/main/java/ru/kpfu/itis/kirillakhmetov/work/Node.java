package ru.kpfu.itis.kirillakhmetov.work;

public class Node {
    private int degree;
    private int[] keys;
    private Node[] childs;
    private boolean leaf;

    public Node(int t) {
        this.keys = new int[2 * t - 1];
        this.childs = new Node[2 * t];
        this.leaf = true;
        this.degree = 0;
    }

    public void insertKey(int key) {
        int i;
        // Циклический сдвиг вправо элементов, больших key
        for (i = degree - 1; i >= 0 && key < keys[i]; i--) {
            keys[i + 1] = keys[i];
        }

        keys[i + 1] = key;
        degree++;
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

    public void setKeys(int[] keys) {
        this.keys = keys;
    }

    public Node getChildByIndex(int index) {
        return childs[index];
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public Node[] getChilds() {
        return childs;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public void setChilds(Node[] childs) {
        this.childs = childs;
    }
}
