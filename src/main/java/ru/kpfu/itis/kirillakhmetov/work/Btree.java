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

    public void show() {
        show(root, 0);
    }

    private void show(Node x, int l) {
        System.out.println("Уровень " + l);
        for (int i = 0; i < x.getDegree(); i++) {
            System.out.print(x.getKeys()[i] + " ");
        }
        System.out.println();
        l++;
        if (!x.isLeaf()) {
            for (int i = 0; i < x.getDegree() + 1; i++) {
                show(x.getChildren()[i], l);
            }
        }
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

//    @Override
//    public void remove(int key) {
//        Node node = search(root, key);
//        if (node != null) {
//            if (node.isLeaf() && node.getDegree() > (order - 1)) {
//                node.removeKey(key);
//                System.out.println(Arrays.toString(node.getKeys()));
//            }
//        } else {
//            throw new RuntimeException("Элемент с таким значением не найден");
//        }
//    }

    private void remove(Node node, int key) {
        int pos = node.find(key);
        if (pos != -1) {
            if (node.isLeaf()) {
                int i = 0;
                for (i = 0; i < node.getDegree() && node.getKeys()[i] != key; i++) {
                }
                ;
                for (; i < node.getDegree(); i++) {
                    if (i != 2 * order - 2) {
                        node.getKeys()[i] = node.getKeys()[i + 1];
                    }
                }
                node.setDegree(node.getDegree() - 1);
                return;
            }
            if (!node.isLeaf()) {

                Node pred = node.getChildren()[pos];
                int predKey = 0;
                if (pred.getDegree() >= order) {
                    for (;;) {
                        if (pred.isLeaf()) {
                            System.out.println(pred.getDegree());
                            predKey = pred.getKeys()[pred.getDegree() - 1];
                            break;
                        } else {
                            pred = pred.getChildren()[pred.getDegree()];
                        }
                    }
                    remove(pred, predKey);
                    node.getKeys()[pos] = predKey;
                    return;
                }

                Node nextNode = node.getChildren()[pos + 1];
                if (nextNode.getDegree() >= order) {
                    int nextKey = nextNode.getKeys()[0];
                    if (!nextNode.isLeaf()) {
                        nextNode = nextNode.getChildren()[0];
                        for (;;) {
                            if (nextNode.isLeaf()) {
                                nextKey = nextNode.getKeys()[nextNode.getDegree() - 1];
                                break;
                            } else {
                                nextNode = nextNode.getChildren()[nextNode.getDegree()];
                            }
                        }
                    }
                    remove(nextNode, nextKey);
                    node.getKeys()[pos] = nextKey;
                    return;
                }

                int temp = pred.getDegree() + 1;
                pred.getKeys()[pred.getDegree() + 1] = node.getKeys()[pos];
                for (int i = 0, j = pred.getDegree(); i < nextNode.getDegree(); i++) {
                    pred.getKeys()[j++] = nextNode.getKeys()[i];
                    pred.setDegree(pred.getDegree() + 1);
                }
                for (int i = 0; i < nextNode.getDegree() + 1; i++) {
                    pred.getChildren()[temp++] = nextNode.getChildren()[i];
                }

                node.getChildren()[pos] = pred;
                for (int i = pos; i < node.getDegree(); i++) {
                    if (i != 2 * order - 2) {
                        node.getKeys()[i] = node.getKeys()[i + 1];
                    }
                }
                for (int i = pos + 1; i < node.getDegree() + 1; i++) {
                    if (i != 2 * order - 1) {
                        node.getChildren()[i] = node.getChildren()[i + 1];
                    }
                }
                node.setDegree(node.getDegree() - 1);
                if (node.getDegree() == 0) {
                    if (node == root) {
                        root = node.getChildren()[0];
                    }
                    node = node.getChildren()[0];
                }
                remove(pred, key);
                return;
            }
        } else {
            for (pos = 0; pos < node.getDegree(); pos++) {
                if (node.getKeys()[pos] > key) {
                    break;
                }
            }
            Node tmp = node.getChildren()[pos];
            if (tmp.getDegree() >= order) {
                remove(tmp, key);
                return;
            }
            Node nb = null;
            int devider = -1;

            if (pos != node.getDegree() && node.getChildren()[pos + 1].getDegree() >= order) {
                devider = node.getKeys()[pos];
                nb = node.getChildren()[pos + 1];
                node.getKeys()[pos] = nb.getKeys()[0];
                tmp.getKeys()[tmp.getDegree() + 1] = devider;
                tmp.getChildren()[tmp.getDegree()] = nb.getChildren()[0];
                for (int i = 1; i < nb.getDegree(); i++) {
                    nb.getKeys()[i - 1] = nb.getKeys()[i];
                }
                for (int i = 1; i <= nb.getDegree(); i++) {
                    nb.getChildren()[i - 1] = nb.getChildren()[i];
                }
                nb.setDegree(nb.getDegree() - 1);
                remove(tmp, key);
                return;
            } else if (pos != 0 && node.getChildren()[pos - 1].getDegree() >= order) {

                devider = node.getKeys()[pos - 1];
                nb = node.getChildren()[pos - 1];
                node.getKeys()[pos - 1] = nb.getKeys()[nb.getDegree() - 1];
                Node child = nb.getChildren()[nb.getDegree()];
                nb.setDegree(nb.getDegree());

                for (int i = tmp.getDegree(); i > 0; i--) {
                    tmp.getKeys()[i] = tmp.getKeys()[i - 1];
                }
                tmp.getKeys()[0] = devider;
                for (int i = tmp.getDegree() + 1; i > 0; i--) {
                    tmp.getChildren()[i] = tmp.getChildren()[i - 1];
                }
                tmp.getChildren()[0] = child;
                tmp.setDegree(tmp.getDegree() + 1);
                remove(tmp, key);
                return;
            } else {
                Node lt = null;
                Node rt = null;
                boolean last = false;
                if (pos != node.getDegree()) {
                    devider = node.getKeys()[pos];
                    lt = node.getChildren()[pos];
                    rt = node.getChildren()[pos + 1];
                } else {
                    devider = node.getKeys()[pos - 1];
                    rt = node.getChildren()[pos];
                    lt = node.getChildren()[pos - 1];
                    last = true;
                    pos--;
                }
                for (int i = pos; i < node.getDegree() - 1; i++) {
                    node.getKeys()[i] = node.getKeys()[i + 1];
                }
                for (int i = pos + 1; i < node.getDegree(); i++) {
                    node.getChildren()[i] = node.getChildren()[i + 1];
                }
                node.setDegree(node.getDegree() - 1);
                lt.getKeys()[lt.getDegree() + 1] = devider;

                for (int i = 0, j = lt.getDegree(); i < rt.getDegree() + 1; i++, j++) {
                    if (i < rt.getDegree()) {
                        lt.getKeys()[j] = rt.getKeys()[i];
                    }
                    lt.getChildren()[j] = rt.getChildren()[i];
                }
                lt.setDegree(lt.getDegree() + rt.getDegree());
                if (node.getDegree() == 0) {
                    if (node == root) {
                        root = node.getChildren()[0];
                    }
                    node = node.getChildren()[0];
                }
                remove(lt, key);
                return;
            }
        }
    }

    public void remove(int key) {
        Node x = search(root, key);
        if (x == null) {
            return;
        }
        remove(root, key);
    }

    public long getCounterOperations() {
        return counterOperations;
    }

    public void setCounterOperations(long counterOperations) {
        this.counterOperations = counterOperations;
    }
}
