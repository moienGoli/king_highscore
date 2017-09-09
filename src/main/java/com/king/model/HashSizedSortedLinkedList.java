package com.king.model;

import java.util.*;

/**
 * Use a HashMap underneath to provide constant time for contains and get(K).
 * It is not thread safe and it will mess up the added functions
 * A convenient getHash() method should be Implemented because contains(), remove() and get() is dependant to it.
 * A size parameter in creation is needed. No adding will be done after reaching the size.
 * I am using LinkedList over ArrayList because:
 * - need constant time for removing
 * - don't want to worry about resizing
 * I am using this over TreeSet because:
 * - now only the add function is O(log n)
 * The reason that i have not extended LinkedList is
 * - there are so many other methods in linkedList that accidental call upon them will mess things up
 * <p>
 * <p>
 * Created by moien on 9/9/17.
 */
public class HashSizedSortedLinkedList<K extends Comparable<K>> {

    private Map<K, Integer> itemIndexMap = new HashMap<>();
    private LinkedList<K> itemList = new LinkedList<>();
    private final int size;

    public HashSizedSortedLinkedList(int size) {
        this.size = size;
    }

    public void add(K k) {

        int index = Collections.binarySearch(itemList, k);
        if (index < size) {
            itemIndexMap.put(k, index);
            itemList.add(index, k);
        }
    }

    public boolean contains(Object o) {
        return itemIndexMap.containsKey(o);
    }

    public boolean remove(Object o) {

        Integer index = itemIndexMap.remove(o);
        if (index != null) {
            return itemList.remove(index);
        } else {
            return false;
        }
    }

    public K get(K k) {

        Integer index = itemIndexMap.get(k);
        if (index != null) {
            return itemList.get(index);
        } else {
            return null;
        }
    }

    public K get(int index) {
        return itemList.get(index);
    }

    public K getLast() {
        return itemList.getLast();
    }

    public int getSize() {
        return itemList.size();
    }

    public List<K> getList() {
        return itemList;
    }
}
