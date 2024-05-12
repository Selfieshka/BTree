package ru.kpfu.itis.kirillakhmetov.work;

public class BTree {
    public final int order;
    private Node root;
    private long counterOperations = 0;

    public BTree() {
        this.order = 2;
        this.root = new Node(order);
    }

    public BTree(int order) {
        if (isValid(order)) {
            this.order = order;
            this.root = new Node(order);
        } else {
            throw new RuntimeException("The \"order\" parameter must be at least 2");
        }
    }

    public void add(int key) {
        Node tempRoot = root;
        counterOperations++;
        if (tempRoot.getDegree() == (2 * order - 1)) {
            Node s = new Node(order);
            root = s;
            s.setLeaf(false);
            s.getChildren()[0] = tempRoot;
            split(s, 0, tempRoot);
            insertValue(s, key);
            counterOperations += 7;
        } else {
            insertValue(tempRoot, key);
            counterOperations++;
        }
        counterOperations += 3;
    }

    private void insertValue(Node node, int key) {
        if (node.isLeaf()) {
            node.insertKey(key);
            counterOperations += node.counterOperations + 1;
        } else {
            int i = 0;
            for (i = node.getDegree() - 1; i >= 0 && key < node.getKeys()[i]; i--) {
                counterOperations += 4;
            }
            i++;
            Node tempNode = node.getChildren()[i];
            if (tempNode.getDegree() == (2 * order - 1)) {
                split(node, i, tempNode);
                counterOperations++;
                if (key > node.getKeys()[i]) {
                    i++;
                    counterOperations += 3;
                }
            }
            insertValue(node.getChildren()[i], key);
            counterOperations += 11;
        }
    }

    private void split(Node x, int index, Node y) {
        Node z = new Node(order);
        z.setDegree(order - 1);
        z.setLeaf(y.isLeaf());
        counterOperations += 5;
        for (int i = 0; i < order - 1; i++) {
            z.getKeys()[i] = y.getKeys()[i + order];
            counterOperations += 7;
        }
        counterOperations++;
        if (!y.isLeaf()) {
            counterOperations++;
            for (int i = 0; i < order; i++) {
                z.getChildren()[i] = y.getChildren()[i + order];
                counterOperations += 6;
            }
            counterOperations++;
        }
        y.setDegree(order - 1);
        counterOperations += 2;
        for (int i = x.getDegree(); i >= index + 1; i--) {
            x.getChildren()[i + 1] = x.getChildren()[i];
            counterOperations += 6;
        }
        counterOperations += 2;
        x.getChildren()[index + 1] = z;
        counterOperations += 3;

        for (int j = x.getDegree() - 1; j >= index; j--) {
            x.getKeys()[j + 1] = x.getKeys()[j];
            counterOperations += 7;
        }
        counterOperations += 3;
        x.getKeys()[index] = y.getKeys()[order - 1];
        x.setDegree(x.getDegree() + 1);
        counterOperations += 7;
    }

    private boolean isValid(int a) {
        return a >= 2;
    }

    private Node search(Node x, int key) {
        int i = 0;
        counterOperations++;
        if (x == null) {
            counterOperations++;
            return x;
        }
        counterOperations++;
        for (i = 0; i < x.getDegree(); i++) {
            counterOperations += 3;
            if (key < x.getKeys()[i]) {
                counterOperations += 2;
                break;
            }
            if (key == x.getKeys()[i]) {
                counterOperations += 2;
                return x;
            }
            counterOperations += 4;
        }
        counterOperations++;
        if (x.isLeaf()) {
            counterOperations++;
            return null;
        } else {
            counterOperations++;
            return search(x.getChildren()[i], key);
        }
    }

    public boolean find(int key) {
        Node node = search(root, key);
        counterOperations++;
        return node != null;
    }


    private void remove(Node node, int key) {
        int pos = node.find(key);
        counterOperations++;
        if (pos != -1) {
            if (node.isLeaf()) {
                int i = 0;
                for (i = 0; i < node.getDegree() && node.getKeys()[i] != key; i++) {
                    counterOperations += 5;
                }
                for (; i < node.getDegree(); i++) {
                    if (i != 2 * order - 2) {
                        node.getKeys()[i] = node.getKeys()[i + 1];
                        counterOperations += 3;
                    }
                    counterOperations++;
                }
                node.setDegree(node.getDegree() - 1);
                counterOperations += 3;
                return;
            }
            if (!node.isLeaf()) {
                Node pred = node.getChildren()[pos];
                int predKey = 0;
                counterOperations += 3;
                if (pred.getDegree() >= order) {
                    for (; ; ) {
                        counterOperations++;
                        if (pred.isLeaf()) {
                            predKey = pred.getKeys()[pred.getDegree() - 1];
                            counterOperations += 3;
                            break;
                        } else {
                            pred = pred.getChildren()[pred.getDegree()];
                            counterOperations += 3;
                        }
                    }
                    remove(pred, predKey);
                    node.getKeys()[pos] = predKey;
                    counterOperations += 3;
                    return;
                }

                Node nextNode = node.getChildren()[pos + 1];
                counterOperations++;
                if (nextNode.getDegree() >= order) {
                    int nextKey = nextNode.getKeys()[0];
                    counterOperations++;
                    if (!nextNode.isLeaf()) {
                        nextNode = nextNode.getChildren()[0];
                        for (; ; ) {
                            counterOperations++;
                            if (nextNode.isLeaf()) {
                                nextKey = nextNode.getKeys()[nextNode.getDegree() - 1];
                                counterOperations += 4;
                                break;
                            } else {
                                nextNode = nextNode.getChildren()[nextNode.getDegree()];
                                counterOperations += 3;
                            }
                        }
                        counterOperations++;
                    }
                    remove(nextNode, nextKey);
                    node.getKeys()[pos] = nextKey;
                    counterOperations += 3;
                    return;
                }

                int temp = pred.getDegree() + 1;
                pred.getKeys()[pred.getDegree()] = node.getKeys()[pos];
                pred.setDegree(pred.getDegree() + 1);
                counterOperations += 9;
                for (int i = 0, j = pred.getDegree(); i < nextNode.getDegree(); i++) {
                    pred.getKeys()[j++] = nextNode.getKeys()[i];
                    pred.setDegree(pred.getDegree() + 1);
                    counterOperations += 9;
                }
                for (int i = 0; i < nextNode.getDegree() + 1; i++) {
                    pred.getChildren()[temp++] = nextNode.getChildren()[i];
                    counterOperations += 6;
                }

                node.getChildren()[pos] = pred;
                counterOperations++;
                for (int i = pos; i < node.getDegree(); i++) {
                    if (i != 2 * order - 2) {
                        node.getKeys()[i] = node.getKeys()[i + 1];
                        counterOperations += 3;
                    }
                    counterOperations += 2;
                }
                for (int i = pos + 1; i < node.getDegree() + 1; i++) {
                    if (i != 2 * order - 1) {
                        node.getChildren()[i] = node.getChildren()[i + 1];
                        counterOperations += 3;
                    }
                    counterOperations++;
                }
                node.setDegree(node.getDegree() - 1);
                counterOperations++;
                if (node.getDegree() == 0) {
                    if (node == root) {
                        root = node.getChildren()[0];
                    }
                    node = node.getChildren()[0];
                    counterOperations += 3;
                }
                remove(pred, key);
                counterOperations++;
            }
        } else {
            for (pos = 0; pos < node.getDegree(); pos++) {
                if (node.getKeys()[pos] > key) {
                    counterOperations += 2;
                    break;
                }
                counterOperations += 2;
            }
            Node tmp = node.getChildren()[pos];
            if (tmp.getDegree() >= order) {
                remove(tmp, key);
                counterOperations += 2;
                return;
            }
            if (true) {
                Node nb = null;
                int devider = -1;
                counterOperations += 2;
                if (pos != node.getDegree() && node.getChildren()[pos + 1].getDegree() >= order) {
                    devider = node.getKeys()[pos];
                    nb = node.getChildren()[pos + 1];
                    node.getKeys()[pos] = nb.getKeys()[0];
                    tmp.getKeys()[tmp.getDegree()] = devider;
                    tmp.setDegree(tmp.getDegree() + 1);
                    tmp.getChildren()[tmp.getDegree()] = nb.getChildren()[0];
                    counterOperations += 20;
                    for (int i = 1; i < nb.getDegree(); i++) {
                        nb.getKeys()[i - 1] = nb.getKeys()[i];
                        counterOperations += 3;
                    }
                    for (int i = 1; i <= nb.getDegree(); i++) {
                        nb.getChildren()[i - 1] = nb.getChildren()[i];
                        counterOperations += 3;
                    }
                    nb.setDegree(nb.getDegree() - 1);
                    remove(tmp, key);
                    counterOperations += 3;
                } else if (pos != 0 && node.getChildren()[pos - 1].getDegree() >= order) {

                    devider = node.getKeys()[pos - 1];
                    nb = node.getChildren()[pos - 1];
                    node.getKeys()[pos - 1] = nb.getKeys()[nb.getDegree() - 1];
                    Node child = nb.getChildren()[nb.getDegree()];

                    nb.setDegree(nb.getDegree() - 1);
                    counterOperations += 14;
                    for (int i = tmp.getDegree(); i > 0; i--) {
                        tmp.getKeys()[i] = tmp.getKeys()[i - 1];
                        counterOperations += 3;
                    }
                    tmp.getKeys()[0] = devider;
                    counterOperations++;
                    for (int i = tmp.getDegree() + 1; i > 0; i--) {
                        tmp.getChildren()[i] = tmp.getChildren()[i - 1];
                        counterOperations += 3;
                    }
                    tmp.getChildren()[0] = child;
                    tmp.setDegree(tmp.getDegree() + 1);
                    remove(tmp, key);
                    counterOperations += 5;
                } else {
                    Node lt = null;
                    Node rt = null;
                    boolean last = false;
                    counterOperations += 3;
                    if (pos != node.getDegree()) {
                        devider = node.getKeys()[pos];
                        lt = node.getChildren()[pos];
                        rt = node.getChildren()[pos + 1];
                        counterOperations += 5;
                    } else {
                        devider = node.getKeys()[pos - 1];
                        rt = node.getChildren()[pos];
                        lt = node.getChildren()[pos - 1];
                        last = true;
                        pos--;
                        counterOperations += 7;
                    }
                    for (int i = pos; i < node.getDegree() - 1; i++) {
                        node.getKeys()[i] = node.getKeys()[i + 1];
                        counterOperations += 3;
                    }
                    for (int i = pos + 1; i < node.getDegree(); i++) {
                        node.getChildren()[i] = node.getChildren()[i + 1];
                        counterOperations += 3;
                    }
                    node.setDegree(node.getDegree() - 1);
                    lt.getKeys()[lt.getDegree()] = devider;
                    lt.setDegree(lt.getDegree() + 1);
                    counterOperations += 8;
                    for (int i = 0, j = lt.getDegree(); i < rt.getDegree() + 1; i++, j++) {
                        if (i < rt.getDegree()) {
                            lt.getKeys()[j] = rt.getKeys()[i];
                            counterOperations += 2;
                        }
                        lt.getChildren()[j] = rt.getChildren()[i];
                        counterOperations += 5;
                    }
                    lt.setDegree(lt.getDegree() + rt.getDegree());
                    counterOperations++;
                    if (node.getDegree() == 0) {
                        if (node == root) {
                            root = node.getChildren()[0];
                            counterOperations++;
                        }
                        node = node.getChildren()[0];
                        counterOperations += 2;
                    }
                    remove(lt, key);
                    counterOperations++;
                }
            }
        }
    }

    public void remove(int key) {
        Node x = search(root, key);
        counterOperations++;
        if (x == null) {
            return;
        }
        counterOperations++;
        remove(root, key);
    }

    public long getCounterOperations() {
        return counterOperations;
    }

    public void setCounterOperations(long counterOperations) {
        this.counterOperations = counterOperations;
    }
}
