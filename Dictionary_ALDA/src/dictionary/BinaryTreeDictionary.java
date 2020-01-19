/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import java.util.Iterator;

/**
 *
 * @author student
 */
public class BinaryTreeDictionary<K extends Comparable<? super K>,V> implements Dictionary<K, V> {
    private V oldValue;
    private int size;
    private Node<K, V> root = null;

    public BinaryTreeDictionary() {
        size = 0;
        oldValue = null;
    }

    @Override
    public V insert(K key, V value) {
        root = insertR(key, value, root);
        if (root != null)
            root.parent = null;
        return oldValue;
    }

    private Node<K,V> insertR(K key, V value, Node<K,V> p) {
        if (p == null) {
            p = new Node(key, value);
            size++;
            oldValue = null;
        }
        else if (key.compareTo(p.entry.getKey()) < 0) {
            p.left = insertR(key, value, p.left);
            if (p.left != null) {
                p.left.parent = p;
            }
        } else if (key.compareTo(p.entry.getKey()) > 0) {
            p.right = insertR(key, value, p.right);
            if (p.right != null) {
                p.right.parent = p;
            }
        } else { // Schlüssel bereits vorhanden:
            oldValue = p.entry.getValue();
            p.entry.setValue(value);
        }
        p = balance(p);
        return p;
    }
    private Node<K,V> balance(Node<K,V> p) {
        if (p == null)
            return null;
        p.height = Math.max(getHeight(p.left), getHeight(p.right)) + 1;
        if (getBalance(p) == -2) {
            if (getBalance(p.left) <= 0)
                p = rotateRight(p);
            else
                p = rotateLeftRight(p);
        }
        else if (getBalance(p) == +2) {
            if (getBalance(p.right) >= 0)
                p = rotateLeft(p);
            else
                p = rotateRightLeft(p);
        }
        return p;
    }

    private Node<K,V> rotateLeftRight(Node<K,V> p) {
        assert p.left != null;
        p.left = rotateLeft(p.left);
        return rotateRight(p);
    }
    private Node<K,V> rotateRightLeft(Node<K,V> p) {
        assert p.right != null;
        p.right = rotateRight(p.right);
        return rotateLeft(p);
    }

    private Node<K,V> rotateRight(Node<K,V> p) {
        assert p.left != null;
        Node<K, V> q = p.left;
        p.left = q.right;
//        if (p.left != null) {
//            p.left.parent = p;
//        }
        q.right = p;
        p.parent = q;
        p.height = Math.max(getHeight(p.left), getHeight(p.right)) + 1;
        q.height = Math.max(getHeight(q.left), getHeight(q.right)) + 1;
        return q;
    }

    private Node<K,V> rotateLeft(Node<K,V> p) {
        assert p.right != null;
        Node<K, V> q = p.right;
        p.right = q.left;
//        if (p.right.parent != null) {
//            p.right.parent = p;
//        }
        q.left = p;
        p.parent = q;
        p.height = Math.max(getHeight(p.right), getHeight(p.left)) + 1;
        q.height = Math.max(getHeight(q.right), getHeight(q.left)) + 1;
        return q;
    }


    @Override
    public V search(K key) {
        return searchR(key, root);
    }

    private V searchR(K key, Node<K,V> p) {
        if (p == null)
            return null;
        else if (key.compareTo(p.entry.getKey()) < 0)
            return searchR(key, p.left);
        else if (key.compareTo(p.entry.getKey()) > 0)
            return searchR(key, p.right);
        else
            return p.entry.getValue();
    }


    public V remove(K key) {
        root = removeR(key, root);
        return oldValue;
    }
    private Node<K,V> removeR(K key, Node<K,V> p) {
        if (p == null) { oldValue = null; }
        else if(key.compareTo(p.entry.getKey()) < 0)
            p.left = removeR(key, p.left);
        else if (key.compareTo(p.entry.getKey()) > 0)
            p.right = removeR(key, p.right);
        else if (p.left == null || p.right == null) {
            // p muss gelöscht werden
            // und hat ein oder kein Kind:
            oldValue = p.entry.getValue();
            Node<K, V> q = p;
            p = (p.left != null) ? p.left : p.right;
            if (p != null) {
                p.parent = q.parent;
            }
            size--;
        } else {
            // p muss gelöscht werden und hat zwei Kinder:
            MinEntry<K,V> min = new MinEntry<K,V>();
            p.right = getRemMinR(p.right, min);
            oldValue = p.entry.getValue();
            Node<K, V> q = p;
            p = new Node<>(min.key, min.value);
            p.parent = q.parent;
            p.left = q.left;
            p.right = q.right;
            size--;
        }
        p = balance(p);
        return p;
    }

    private Node<K,V> getRemMinR(Node<K,V> p, MinEntry<K,V> min) {
        assert p != null;
        if (p.left == null) {
            min.key = p.entry.getKey();
            min.value = p.entry.getValue();
            p = p.right;
        }
        else
            p.left = getRemMinR(p.left, min);
        return p;
    }
    private static class MinEntry<K, V> {
        private K key;
        private V value;
    }

    @Override
    public int size() {
        return size;
    }

    private Node<K, V> leftMostDescendant(Node<K, V> p) {
        assert p != null;
        while(p.left != null) {
            p = p.left;
        }
        return p;
    }

    private Node<K, V> parentOfLeftMostAncestor(Node<K, V> p) {
        assert p != null;
        while(p.parent != null && p.parent.right == p) {
            p = p.parent;
        }
        return p.parent;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new BinaryTreeDictionaryIterator();
    }

    private class BinaryTreeDictionaryIterator implements Iterator<Dictionary.Entry<K, V>>  {
        private Node<K, V> p;
        private Node<K, V> current = leftMostDescendant(root);
        @Override
        public boolean hasNext() {
            if (current != null) {
                return true;
            }
            return false;
        }

        @Override
        public Entry<K, V> next() {
            while (current != null) {
                p = current;
                if (current.right != null) {
                    current = leftMostDescendant(current.right);
                } else {
                    current = parentOfLeftMostAncestor(current);
                }
                return p.entry;
            }
            return null;
        }
    }
    
    public void prettyPrint() {
       prettyPrintR(root, 0);
    }

    private void prettyPrintR(Node<K, V> p, int d) {
        if (p == null) {
            for(int i = 0; i < d; i++) {
                System.out.print("  ");
            }
            System.out.println("*");
            return;
        }
        for(int i = 0; i < d; i++) {
            System.out.print("  ");
        }
        System.out.println(p.entry.getKey());

        prettyPrintR(p.left, d + 1);
        prettyPrintR(p.right, d + 1);
    }

    static private class Node<K, V> {
        private Node<K, V> parent; // Elternzeiger
        private Entry<K, V> entry;
        private Node<K, V> left;
        private Node<K, V> right;
        int height;

        private Node(K k, V v) {
            height = 0;
            entry = new Entry<>(k, v);
            left = null;
            right = null;
            parent = null;
        }
    }

    private int getHeight(Node<K,V> p) {
        if (p == null)
            return -1;
        else
            return p.height;
    }
    private int getBalance(Node<K,V> p) {
        if (p == null)
            return 0;
        else
            return getHeight(p.right) - getHeight(p.left);
    }
}
