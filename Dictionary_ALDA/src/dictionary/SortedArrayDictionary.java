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
public class SortedArrayDictionary<K extends Comparable<? super K>,V> implements Dictionary<K,V> {
    private int size;
    Entry<K,V>[] data;
    private static final int DEF_CAPACITY = 32;
    
    @SuppressWarnings("unchecked")
    public SortedArrayDictionary() {
        size = 0;
        data = new Entry[DEF_CAPACITY];
    }
    
    @SuppressWarnings("unchecked")
    private void ensureCapacity(int newCapacity) {
        if(newCapacity < size) {
            return;
        }
        Entry[] old = data;
        data = new Entry[newCapacity];
        System.arraycopy(old, 0, data, 0, size);
    }

    @Override
    public V insert(K key, V value) {
        int i = searchKey(key);
        if (i != -1) {
            V r = data[i].getValue();
            data[i].setValue(value);
            return r;
        }
        
        if (data.length == size) {
            ensureCapacity(2*size);
        }
        
        int j = size - 1;
        while (j >= 0 && key.compareTo(data[j].getKey()) < 0) {
            data[j+1] = data[j];
            j--;
        }
        data[j+1] = new Entry<>(key,value);
        size++;
        return null;
    }
    
    public int searchKey(K key) {
        int li = 0;
        int re = size - 1;
        while (re >= li) {
            int m = (li + re)/2;
            if (key.compareTo(data[m].getKey()) < 0) {
                re = m - 1;
            } else if (key.compareTo(data[m].getKey()) > 0) {
                li = m + 1;
            } else {
                return m;
            }
        }
        return -1;
    }

    @Override
    public V search(K key) {
        int i = searchKey(key);
        int li = 0;
        int re = size - 1;
        while (re >= li) {
            int m = (li + re)/2;
            if (key.compareTo(data[m].getKey()) < 0)
                re = m - 1; 
            else if (key.compareTo(data[m].getKey()) > 0)
                li = m + 1;
            else
                return data[m].getValue();
        }
        return null;
    }

    @Override
    public V remove(K key) {
        int i = searchKey(key);
        if (i == -1)
            return null;
        V r = data[i].getValue();
        for (int j = i; j < size - 1; j++) {
            data[j] = data[j+1];
        }
        data[--size] = null;
        return r;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Dictionary.Entry<K, V>> iterator() {
        return new SortedArrayDictionaryIterator();
    }
    
    private class SortedArrayDictionaryIterator implements Iterator<Dictionary.Entry<K, V>> {
        int i = -1;
        private Entry<K, V> current = data[i + 1];
        
        @Override
        public boolean hasNext() {
            return size > i + 1;
        }

        @Override
        public Entry<K, V> next() {
            return data[++i];
        }
        
    }
            
}
