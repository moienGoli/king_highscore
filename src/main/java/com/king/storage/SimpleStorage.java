package com.king.storage;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Receives and stores entries from users.
 * <p>
 * <p>
 * Created by moien on 9/8/17.
 */
public class SimpleStorage<K extends Comparable<K>> {
    ;
    private Queue<K> entries = new ConcurrentLinkedQueue<>();

    public void add(K k) {
        entries.add(k);
    }

    public Queue<K> retrieveAll() {
        return entries;
    }
}