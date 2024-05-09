package ru.kpfu.itis.kirillakhmetov.work;

public class Btree {
    public final int order;
    private Node root;

    public Btree(int order) {
        if (isValid(order)) {
            this.order = order;
            this.root = new Node(order);
        } else {
            throw new RuntimeException("The \"order\" parameter must be at least 2");
        }
    }

    public void add(int key) {
        Node tempRoot = root;
        if (tempRoot.getDegree() == (2 * order  - 1)) {
            Node s = new Node(order);
            root = s;
            s.setLeaf(false);
            split(s, 0, tempRoot);
            insertValue(s, key);
        } else {
            insertValue(tempRoot, key);
        }
    }

    private void insertValue(Node node, int key) {
        if (node.isLeaf()) {
            node.insertKey(key);
        } else {
            int[] nodeKeys = node.getKeys();
            int i = node.getDegree() - 1;
            while (i >= 0 && key < nodeKeys[i]) {
                i--;
            }
            i++;
            Node tempNode = node.getChildByIndex(i);
            if (tempNode.getDegree() == (2 * order - 1)) {
                split(node, i, tempNode);
                if (key > nodeKeys[i]) {
                    i++;
                }
            }
            insertValue(node.getChildByIndex(i), key);
        }
    }

    private void split(Node x, int index, Node y) {
        Node z = new Node(order - 1);
        z.setLeaf(y.isLeaf());

        int[] zKeys = z.getKeys();
        int[] yKeys = y.getKeys();
        int[] xKeys = x.getKeys();
        Node[] zChilds = z.getChilds();
        Node[] yChilds = y.getChilds();
        Node[] xChilds = x.getChilds();

        for (int i = 0; i < order - 1; i++) {
            zKeys[i] = yKeys[i + order];
        }
        if (!y.isLeaf()) {
            for (int i = 0; i < order; i++) {
                zChilds[i] = yChilds[i + order];
            }
        }
        y.setDegree(order - 1);
        for (int i = x.getDegree(); i >= index + 1; i--) {
            xChilds[i + 1] = xChilds[i];
        }
        xChilds[index + 1] = z;

        for (int j = x.getDegree() - 1; j >= index; j--) {
            xKeys[j + 1] = xKeys[j];
        }
        xKeys[index] = yKeys[order - 1];
        x.setDegree(x.getDegree() + 1);

        x.setKeys(xKeys);
        y.setKeys(yKeys);
        z.setKeys(zKeys);
        x.setChilds(xChilds);
        x.setChilds(yChilds);
        x.setChilds(zChilds);
    }

    private boolean isValid(int a) {
        return a >= 2;
    }

    public void show() {
        show(root);
    }

    private void show(Node x) {
        for (int i = 0; i < x.getDegree(); i++) {
            System.out.print(x.getKeys()[i] + " ");
        }
        if (!x.isLeaf()) {
            for (int i = 0; i < x.getDegree() + 1; i++) {
                show(x.getChilds()[i]);
            }
        }
    }
}
