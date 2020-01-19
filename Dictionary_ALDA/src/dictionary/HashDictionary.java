/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author student
 */
public class HashDictionary<K, V> implements Dictionary<K, V> {
    public int size;
    LinkedList<Entry<K, V>>[] tab;
    private int hashkey;
    private final int MAX_FILLING_LEVEL = 2;
    
    public int getHashkey() {
        return hashkey;
    }
    
    @SuppressWarnings("unchecked")
    public HashDictionary(int n) {
        size = 0;
        tab = new LinkedList[n];
        hashkey = n;
    }
    
     @SuppressWarnings("unchecked")
    private void ensureCapacity(int newCapacity) {
        LinkedList<Entry<K, V>>[] old = tab;
        while (!isPrim(newCapacity)) {
            newCapacity++;
        }
        HashDictionary<K, V> neu = new HashDictionary<>(3);
        for(Dictionary.Entry<K,V> e : this) {
            neu.insert(e.getKey(), e.getValue());
        }
        tab = new LinkedList[newCapacity];
        this.hashkey = newCapacity;
        for(Dictionary.Entry<K, V> e : neu) {
            this.insert(e.getKey(), e.getValue());
        }

    }
    
    private boolean isPrim(int n) {
        for (int i = 2; i*i <= n; i++) {
            if (n%i == 0) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public V search (K key) {
        int h = Math.abs(key.hashCode()) % hashkey;
        if (tab[h] == null) {
            return null;
        }
        for (Dictionary.Entry<K, V> entry : tab[h]){
            if (key.equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }


    @Override
    public V insert(K key, V value) {
        int h = Math.abs(key.hashCode()) % hashkey;
        V m = search(key);
        if ((double) size / (double) hashkey > MAX_FILLING_LEVEL) {
            ensureCapacity(2 * hashkey);
        }
        if (m != null) {
            remove(key);
            tab[h].add(new Entry<>(key, value));
            size++;
        } else {
            if (tab[h] == null) {
                tab[h] = new LinkedList<>();
            }
            tab[h].add(new Entry<>(key, value));
            size++;
            return null;
        }
        return m;
    }

    @Override
    public V remove(K key) {
        int h = Math.abs(key.hashCode()) % hashkey;
        V oldValue = null;
        int index = 0;
        if (tab[h] == null) {
            return null;
        }
        if(search(key) != null) {
            for(Dictionary.Entry<K,V> e : tab[h]) {
                if(key.equals(e.getKey())) {
                    oldValue = e.getValue();
                    break;
                }
                index++;
            }
            tab[h].remove(index);
            size--;
        }

        return oldValue;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new HashDictionaryIterator();
    }
    
    private class HashDictionaryIterator implements Iterator<Dictionary.Entry<K, V>>  {
        private int i = -1;
        Iterator<Entry<K,V>> it;
        @Override
        public boolean hasNext() {
            if(it != null && it.hasNext()) {
                return true;
            }
            while(++i < tab.length) {
                if(tab[i] != null) {
                    it = tab[i].iterator();
                    return it.hasNext();
                }
            }
            return false;
        }

        @Override
        public Entry<K, V> next() {
            return it.next();
        }
        
    }
    
}
